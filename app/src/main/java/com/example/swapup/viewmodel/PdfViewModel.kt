package com.example.swapup.viewmodel

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.collection.LruCache
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapup.viewmodel.state.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

private val renderLock = Any()

class PdfViewModel : ViewModel() {
    private val _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState: State<UiState> get() = _uiState

    private var pdfUrl = mutableStateOf<String?>("http://handybook.uz/frontend/web/file/911697626217.pdf")

    private var pdfFile = mutableStateOf<File?>(null)

    private var pdfRenderer = mutableStateOf<PdfRenderer?>(null)

    var lastViewedPage by mutableIntStateOf(0)
        private set

    private var isRendererClosed = false

    private val cache = LruCache<Int, Bitmap>(10)
    private val renderJobs = mutableMapOf<Int, Job>()

    fun downloadPdf(context: Context) {
        viewModelScope.launch {
            Log.d("pdfUrl","${pdfUrl.value}")
            _uiState.value = UiState.Loading
            val result = runCatching {
                withContext(Dispatchers.IO) {
                    downloadPdfFile(context, pdfUrl.value!!)
                }
            }

            result.onSuccess { file ->
                pdfFile.value = file
                _uiState.value = UiState.Success
                Log.d("uiState","${_uiState.value}")
                Log.d("pdfFile","${pdfFile.value}")
                Log.d("pdfRenderer","${pdfRenderer.value}")
                isRendererClosed = false
            }.onFailure { error ->
                Log.d("pdfDownloadFailed?","${result.isFailure}")
                error.printStackTrace()
                _uiState.value = UiState.Error(error.message.toString())
            }
            Log.d("pdfDownloadResult","${result}")
        }
    }

    fun updateLastViewedPage(index: Int) {
        lastViewedPage = index
    }

    fun sendUrl(url:String){
        pdfUrl.value = url
    }

    fun close(){
        pdfRenderer.value?.close()
    }

    fun getCachedBitmap(index: Int): Bitmap? = cache.get(index)

    fun renderPageAsync(
        scope: CoroutineScope,
        index: Int,
        onRendered: (Bitmap) -> Unit
    ) {
        if (cache.get(index) != null || renderJobs[index]?.isActive == true || isRendererClosed) return

        val job = scope.launch(Dispatchers.IO) {
            val rendered = renderPage(pdfRenderer.value!!, index)
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
        isRendererClosed = true
    }


    fun openPdfRenderer() {
        Log.d("pdfFile2","${pdfFile.value}")
        val descriptor = ParcelFileDescriptor.open(pdfFile.value, ParcelFileDescriptor.MODE_READ_ONLY)
        pdfRenderer.value = PdfRenderer(descriptor)
        Log.d("pdfRenderer2","${pdfRenderer.value}")
    }

    private fun renderPage(renderer: PdfRenderer, index: Int): Bitmap {
        synchronized(renderLock) {
            renderer.openPage(index).use { page ->
                val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                return bitmap
            }
        }
    }

    private fun downloadPdfFile(context: Context, urlStr: String): File {
        try {
            val url = URL(urlStr)
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            connection.connect()

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw Exception("HTTP error code: ${connection.responseCode}")
            }

            val file = File.createTempFile("temp_pdf", ".pdf", context.cacheDir)
            file.outputStream().use { output ->
                connection.inputStream.use { input ->
                    input.copyTo(output)
                }
            }
            return file
        }catch (e: Exception){
            println("❌ downloadPdfFile() failed: ${e.message}")
            throw e
        }
    }

    fun pageCount(): Int{
        return pdfRenderer.value?.pageCount?:0
    }
}