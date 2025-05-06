package com.example.swapup.ui.screens

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.swapup.viewmodel.PdfViewModel
import com.example.swapup.viewmodel.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

//@Composable
//fun PdfViewerScreen(){
//    val context = LocalContext.current
//    val vm: PdfViewModel = viewModel()
//
//    when(vm.uiState.value) {
//        UiState.Loading -> {
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Text("Downloading PDF...")
//            }
//        }
//        UiState.Success -> {
//            PdfViewerScreen(pdfFile = pdfFile!!, navController = navController, initialPage = initialPage, viewModel = viewModel)
//        }
//        else -> {
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Text("Failed to download PDF.")
//            }
//        }
//    }
//}
//
//@Composable
//fun PdfViewerScreen(pdfFile: File, navController: NavController, initialPage: Int, viewModel: PdfViewModel) {
//    val context = LocalContext.current
//    val pdfRenderer = remember { openPdfRenderer(context, pdfFile) }
//    val pageCount = remember { pdfRenderer.pageCount }
//    val scope = rememberCoroutineScope()
//    val listState = rememberLazyListState()
//
//    DisposableEffect(Unit) {
//        onDispose {
//            viewModel.clearJobs()
//            pdfRenderer.close()
//        }
//    }
//
//
//    LaunchedEffect(initialPage) {
//        listState.animateScrollToItem(initialPage)
//    }
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        Box(
//            contentAlignment = Alignment.Center
//        ){
//            LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
//                itemsIndexed(List(pageCount) { it }) { index, _ ->
//                    var bitmap by remember(index) { mutableStateOf<Bitmap?>(null) }
//
//                    LaunchedEffect(index) {
//                        val cached = viewModel.getCachedBitmap(index)
//                        if (cached != null) {
//                            bitmap = cached
//                        } else {
//                            viewModel.renderPageAsync(scope, pdfRenderer, index) {
//                                bitmap = it
//                            }
//                        }
//                    }
//
//                    Log.d(TAG, "PdfViewerScreen: ${bitmap}")
//                    if(bitmap == null){
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(400.dp)
//                                .padding(16.dp)
//                        ) {
//                            Text(
//                                "Loading page $index...",
//                                modifier = Modifier.align(Alignment.CenterStart)
//                            )
//                        }
//                    }else{
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .aspectRatio(bitmap!!.width.toFloat() / bitmap!!.height.toFloat())
//                                .padding(vertical = 8.dp)
//                        ) {
//                            Image(
//                                bitmap = bitmap!!.asImageBitmap(),
//                                contentDescription = "Page $index",
//                                contentScale = ContentScale.FillWidth,
//                                modifier = Modifier.fillMaxSize()
//                            )
//                        }
//                    }
//                }
//            }
//
//            Button(onClick = {
//                viewModel.updateLastViewedPage(listState.firstVisibleItemIndex)
//                navController.popBackStack()
//            }, modifier = Modifier.padding(8.dp)) {
//                Text("Close PDF")
//            }
//        }
//    }
//}