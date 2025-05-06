package com.example.swapup.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.collection.LruCache
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapup.data.repository.PdfRepository
import com.example.swapup.viewmodel.state.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

private val renderLock = Any()

class PdfViewModel(private val pdfRepository: PdfRepository) : ViewModel() {
    private val _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState: State<UiState> get() = _uiState

    var pdfUrl = mutableStateOf<String?>(null)

    private var file = mutableStateOf<File?>(null)

    private var pdfRenderer = mutableStateOf<PdfRenderer?>(null)

    var lastViewedPage by mutableIntStateOf(0)
        private set

    private val cache = LruCache<Int, Bitmap>(10)
    private val renderJobs = mutableMapOf<Int, Job>()

    fun updateLastViewedPage(index: Int) {
        lastViewedPage = index
    }

    fun getCachedBitmap(index: Int): Bitmap? = cache.get(index)

    fun renderPageAsync(
        scope: CoroutineScope,
        renderer: PdfRenderer,
        index: Int,
        onRendered: (Bitmap) -> Unit
    ) {
        if (cache.get(index) != null || renderJobs[index]?.isActive == true) return

        val job = scope.launch(Dispatchers.IO) {
            val rendered = renderPage(renderer, index)
            cache.put(index, rendered)
            withContext(Dispatchers.Main) {
                onRendered(rendered)
            }
        }
        renderJobs[index] = job
    }

    fun clearJobs() {
        renderJobs.values.forEach { it.cancel() }
        renderJobs.clear()
    }


    fun openPdfRenderer(context: Context) {
        val descriptor = ParcelFileDescriptor.open(file.value, ParcelFileDescriptor.MODE_READ_ONLY)
        pdfRenderer.value = PdfRenderer(descriptor)
    }

    fun renderPage(renderer: PdfRenderer, index: Int): Bitmap {
        synchronized(renderLock) {
            renderer.openPage(index).use { page ->
                val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                return bitmap
            }
        }
    }

    fun downloadFile(context: Context){
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                val downloadedFile = withContext(Dispatchers.IO) {
                    pdfUrl.value?.let { pdfRepository.downloadPdfFile(context, it) }
                }
                file.value = downloadedFile
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message.toString())
                e.printStackTrace()
            }
        }
    }

    fun pageCount(): Int{
        return pdfRenderer.value?.pageCount?:0
    }
}