package com.example.swapup.ui.screens

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.swapup.R
import com.example.swapup.data.model.Book
import com.example.swapup.data.model.Comment
import com.example.swapup.navigation.Routes
import com.example.swapup.ui.theme.DarkBlue
import com.example.swapup.ui.theme.SkyBlue
import com.example.swapup.viewmodel.BookViewModel
import com.example.swapup.viewmodel.FireStoreViewModel
import com.example.swapup.viewmodel.PdfViewModel
import com.example.swapup.viewmodel.state.UiState

@Composable
fun InfoScreen(
    navController: NavHostController,
    vm: FireStoreViewModel,
    bookVM: BookViewModel,
    pdfVM: PdfViewModel,
) {

    val bookList by bookVM.bookList.observeAsState(emptyList())
    val selectedBook = bookVM.selectedBook
    val book = bookList.find { it.id == selectedBook }!!
    val isBookSaved by vm.isBookSaved.observeAsState(false)

    val uiState by vm.uiState
    val context = LocalContext.current
    when(uiState){
        is UiState.Loading->{
            LoadingScreen()
        }
        is UiState.Error ->{
            val errorMsg = (uiState as UiState.Error).msg
            Toast.makeText(context,errorMsg, Toast.LENGTH_SHORT).show()
        }
        else -> {

        }
    }

    val bookType = vm.bookType
    val bookInfo = vm.bookInfo

    val comments by vm.commentList.observeAsState(emptyList())

    Scaffold (
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBlue)
                    .padding(horizontal = 8.dp)
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        navController.navigate(Routes.Home.name)
                    }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        "Back",
                        tint = Color.White
                    )
                }
                Text("Batafsil", fontSize = 22.sp, color = Color.White)
                IconButton(
                    onClick = {
                        if(isBookSaved){
                            vm.unsaveBook()
                        }
                        else{
                            vm.saveBook()
                        }
                    }
                ) {
                    if(isBookSaved){
                        Icon(
                            painterResource(R.drawable.saved_filled),
                            "Favourite",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    else{
                        Icon(
                            painterResource(R.drawable.saved_border),
                            "Favourite",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if(bookInfo == "Tavsif"){
                if (bookType == "E-Book") {
                    Card(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        onClick = {
                            pdfVM.sendUrl(book.file)
                            navController.navigate(Routes.Pdf.name)
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .weight(8f)
                                    .background(DarkBlue)
                                    .height(48.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(if(pdfVM.lastViewedPage == 0) " O'qishni boshlash " else " O'qishni davom ettirish", color = SkyBlue, fontSize = 18.sp)
                            }
                            if(pdfVM.lastViewedPage != 0 && pdfVM.pageCount() != 0){
                                Row(
                                    modifier = Modifier
                                        .weight(2f)
                                        .background(SkyBlue)
                                        .height(48.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("${((pdfVM.lastViewedPage).toFloat()/(pdfVM.pageCount()).toFloat()*100).toInt()}%", color = DarkBlue, fontSize = 18.sp)
                                }
                            }
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        /*TODO: AUDIO PLAYER FOR BOOK HERE!!!!!*/
                    }
                }
            }else if(bookInfo == "Sharh"){
                Row (
                    modifier = Modifier.fillMaxWidth(0.9f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Column (
                        horizontalAlignment = Alignment.Start
                    ){
                        Row (
                            verticalAlignment = Alignment.Bottom
                        ){
                            Text("${book.reyting}.0", color = DarkBlue, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("52 ta sharhlar", color = Color.LightGray, fontSize = 6.sp, modifier = Modifier.offset(y = (-4).dp))
                        }
                        Row {
                            for(i in 1..book.reyting){
                                Icon(Icons.Default.Star, "Rating", tint = SkyBlue, modifier = Modifier.size(16.dp))
                            }
                            for(i in 1..(5-book.reyting)){
                                Icon(Icons.Default.Star, "Rating", tint = Color.LightGray, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                    Button(
                        onClick = {
                            navController.navigate(Routes.Comment.name)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SkyBlue,
                            contentColor = DarkBlue
                        ),
                        modifier = Modifier.height(48.dp),
                        shape = RoundedCornerShape(9.dp)
                    ) {
                        Text("O'z sharhingizni yozib qoldiring", fontSize = 10.sp)
                    }
                }
            }
        },
        bottomBar = {
            if(bookInfo == "Iqtibos"){ BottomNavigationBar(navController) }
        },
        modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)
    ){paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = if (bookInfo != "Iqtibos") 74.dp else 0.dp)
                ) {
                    item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height((LocalConfiguration.current.screenHeightDp * 0.60).dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .background(Color.Transparent),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.48f)
                                        .background(DarkBlue),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer(modifier = Modifier.height(32.dp))
                                    //Ebook and Audiobook button
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(0x4DB8E8F2)
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(0.9f)
                                                .padding(vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceEvenly
                                        ) {
                                            Button(
                                                onClick = {
                                                    vm.changeBookType("E-Book")
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (bookType == "E-Book") DarkBlue else Color.Transparent
                                                ),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.fillMaxWidth(0.48f)
                                            ) {
                                                Text("E-Kitob")
                                            }
                                            Button(
                                                onClick = {
                                                    vm.changeBookType("AudioBook")
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (bookType == "AudioBook") DarkBlue else Color.Transparent
                                                ),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.fillMaxWidth(0.85f)
                                            ) {
                                                Text("Audio Kitob")
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(100.dp))
                                Text(
                                    book.name,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.W600,
                                    color = DarkBlue
                                )
                                Text(book.author, color = Color.LightGray)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Star, "Rating", tint = SkyBlue)
                                    Text("${book.reyting}.0", color = DarkBlue)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(0.95f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    TextButton(
                                        onClick = {
                                            vm.changeBookInfo("Tavsif")
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = if (bookInfo == "Tavsif") SkyBlue else DarkBlue,
                                            containerColor = Color.Transparent
                                        )
                                    ) {
                                        Text("Tavsifi", fontSize = 18.sp)
                                    }
                                    TextButton(
                                        onClick = {
                                            vm.changeBookInfo("Sharh")
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = if (bookInfo == "Sharh") SkyBlue else DarkBlue,
                                            containerColor = Color.Transparent
                                        )
                                    ) {
                                        Text("Sharhlar", fontSize = 18.sp)
                                    }
                                    TextButton(
                                        onClick = {
                                            vm.changeBookInfo("Iqtibos")

                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = if (bookInfo == "Iqtibos") SkyBlue else DarkBlue,
                                            containerColor = Color.Transparent
                                        )
                                    ) {
                                        Text("Iqtiboslar", fontSize = 18.sp)
                                    }
                                }
                            }
                            /*book image part*/
                            if (bookType == "E-Book") {
                                ImageLoader(
                                    context = LocalContext.current,
                                    imageUrl = book.image,
                                    modifier = Modifier
                                        .size((LocalConfiguration.current.screenHeightDp / 3.75).dp)
                                        .offset(y = (-24).dp),
                                    contentScale = ContentScale.Fit
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size((LocalConfiguration.current.screenHeightDp / 4.5).dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ImageLoader(
                                        context = LocalContext.current,
                                        imageUrl = book.image,
                                        modifier = Modifier.size((LocalConfiguration.current.screenHeightDp / 4.5).dp),
                                        contentScale = ContentScale.FillWidth
                                    )
                                    Box(
                                        modifier = Modifier
                                            .background(Color.White, CircleShape)
                                            .size(24.dp)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0x80FFFFFF), CircleShape)
                                            .size(48.dp)
                                            .border(0.5.dp, Color.White, CircleShape)
                                    )
                                }
                            }
                        }
                    }
                    if(
                        bookInfo == "Tavsif"
                    ) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(0.75f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Icon(
                                        painterResource(R.drawable.outline_insert_drive_file_24),
                                        "bet",
                                        tint = Color.LightGray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text("${book.count_page} bet", color = Color.LightGray, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Icon(
                                        painterResource(R.drawable.book),
                                        "vaqt",
                                        tint = Color.LightGray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text("${book.year} yil", color = Color.LightGray, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Icon(
                                        painterResource(R.drawable.outline_language_24),
                                        "til",
                                        tint = Color.LightGray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        book.lang,
                                        color = Color.LightGray,
                                        fontSize = 12.sp,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1
                                    )
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    book.description,
                                    color = DarkBlue,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.graphicsLayer { alpha = 0.99F })
                                Spacer(modifier = Modifier.size(24.dp))
                            }
                        }
                    }else if(
                        bookInfo == "Sharh"
                    ){
                        items(comments){
                            Spacer(modifier = Modifier.height(24.dp))
                            CommentCard(
                                comment = it
                            )
                        }
                    }else{
                        items(23){
                            Spacer(modifier = Modifier.height(16.dp))
                            QuoteCard()
                        }
                    }
                }
            }
            if(bookInfo == "Tavsif"){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-70).dp)
                        .height(150.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.White)
                            )
                        )
                )
            }
        }
    }
}

@Composable
fun CommentCard(
    comment: Comment
){
    Card(
        modifier = Modifier.padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ){
        Column (
            modifier = Modifier.padding(horizontal = 8.dp)
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Row{
                    Icon(Icons.Default.AccountCircle, "comment", modifier = Modifier.size(56.dp), tint = DarkBlue)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(comment.username, color = DarkBlue, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(Icons.Default.Star, "Rating", tint = SkyBlue, modifier = Modifier.size(20.dp))
                            Text("5.0", color = DarkBlue, fontSize = 14.sp)
                        }
                    }
                }
                Text("22-may 2022-yil", color = Color.LightGray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(comment.text, color = DarkBlue)
        }
    }
}

@Composable
fun QuoteCard(){
    Card(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Iaculis urna id volutpat lacus laoreet non curabitur gravida. Fames ac turpis egestas maecenas. Bibendum enim facilisis gravida neque convallis. Tincidunt dui ut ornare lectus. Accumsan tortor posuere ac ut consequat semper viverra nam.Faucibus et.Bibendum enim facilisis gravida neque convallis. ", fontSize = 10.sp, color = DarkBlue)
                Spacer(modifier = Modifier.height(8.dp))
                Text("123-sahifa", color = Color.LightGray, fontSize = 10.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {}
            ) {
                Icon(painterResource(R.drawable.baseline_bookmark_24), "quote", modifier = Modifier.size(36.dp), tint = DarkBlue)
            }
        }
    }
}