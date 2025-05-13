package com.example.swapup.ui.screens

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.swapup.R
import com.example.swapup.ui.theme.DarkBlue
import com.example.swapup.viewmodel.PdfViewModel
import com.example.swapup.viewmodel.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PdfViewerScreenUrl(
    navController: NavHostController,
    vm: PdfViewModel,
    bookId: Int,
    pdfUrl: String
){
    val context = LocalContext.current
    val uiState by vm.uiState

    LaunchedEffect(Unit) {
        vm.downloadPdf(pdfUrl, context)
    }

    LaunchedEffect(uiState) {
        if (uiState is UiState.Error) {
            val errorMessage = (uiState as UiState.Error).msg
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    when(uiState) {
        is UiState.Loading-> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Downloading PDF...")
            }
        }
        is UiState.Success-> {
            PdfViewerScreen(vm = vm, navController = navController, bookId = bookId)
        }
        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Failed to download PDF...")
            }
        }
        is UiState.Idle->{

        }

    }

}

@Composable
fun PdfViewerScreen(vm: PdfViewModel, navController: NavController, bookId: Int) {
    vm.openPdfRenderer()
    val pageCount = remember { vm.pageCount() }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    DisposableEffect(Unit) {
        onDispose {
            vm.clearJobs()
            scope.launch {
                delay(100) // Let job cancellations settle
                vm.close()
            }
        }
    }


    LaunchedEffect(vm.getBookInfo(bookId)) {
        listState.animateScrollToItem(vm.getBookInfo(bookId))
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(horizontal = 8.dp)
                    .padding(top = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        vm.saveBookInfo(bookId, listState.firstVisibleItemIndex)
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        "Back",
                        tint = DarkBlue
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets.systemBars
    ) {paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
                    itemsIndexed(List(pageCount) { it }) { index, _ ->
                        var bitmap by remember(index) { mutableStateOf<Bitmap?>(null) }

                        LaunchedEffect(index) {
                            val cached = vm.getCachedBitmap(index)
                            if (cached != null) {
                                bitmap = cached
                            } else {
                                vm.renderPageAsync(scope, index) {
                                    bitmap = it
                                }
                            }
                        }

                        Log.d(TAG, "PdfViewerScreen: $bitmap")
                        if (bitmap == null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    "Loading page $index...",
                                    modifier = Modifier.align(Alignment.CenterStart)
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(bitmap!!.width.toFloat() / bitmap!!.height.toFloat())
                                    .padding(vertical = 8.dp)
                            ) {
                                Image(
                                    bitmap = bitmap!!.asImageBitmap(),
                                    contentDescription = "Page $index",
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}