package com.example.handybook.screens

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.handybook.data.model.Book
import com.example.handybook.ui.theme.DarkBlue
import com.example.handybook.ui.theme.SkyBlue
import com.example.handybook.viewmodel.BookViewModel
import kotlinx.coroutines.delay

@Composable
fun CategoryScreen(
    navController: NavHostController,
    bookVM: BookViewModel
) {
    val selectedCategory = bookVM.selectedCategory
    LaunchedEffect(true) {
        delay(500)
        bookVM.fetchBooks(selectedCategory)
    }
    val bookList by bookVM.bookList.observeAsState(emptyList())
    val context = LocalContext.current

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(bookList){book->
            CategoryBookCard(
                book = book,
                context = context,
                onClick = { bookId->
                }
            )
        }
    }
}

@Composable
fun CategoryBookCard(
    book: Book,
    context: Context,
    onClick:(Int) -> Unit,
){
    Card(
        modifier = Modifier.size(180.dp, 300.dp),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
//        border = BorderStroke(1.dp, Color.Gray),
        onClick = {onClick(book.id)},
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(4.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp),
            ){
                ImageLoader(
                    context = context,
                    imageUrl = book.image,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                        .align(Alignment.Center)
                )
                Card(
                    modifier = Modifier.align(Alignment.TopEnd).padding(6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = SkyBlue
                        )
                        Text(
                            text = "${book.reyting}.0",
                            color = DarkBlue,
                            fontWeight = FontWeight.W400,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            Text(
                text = book.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                color = DarkBlue
            )
            Text(
                text = book.author,
                fontSize = 14.sp,
                fontWeight = FontWeight.W400,
                color = Color.LightGray
            )
            Text(
                text = "Year: ${book.year}",
                color = SkyBlue,
                fontSize = 16.sp,
                fontWeight = FontWeight.W600
            )
        }
    }
}