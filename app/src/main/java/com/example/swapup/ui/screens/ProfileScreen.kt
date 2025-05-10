package com.example.swapup.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.swapup.data.model.Book
import com.example.swapup.data.model.User
import com.example.swapup.navigation.Routes
import com.example.swapup.ui.theme.DarkBlue
import com.example.swapup.ui.theme.SkyBlue
import com.example.swapup.viewmodel.BookViewModel
import com.example.swapup.viewmodel.ProfileViewModel
import com.example.swapup.viewmodel.state.UiState

@Composable
fun ProfileScreen(
    navController: NavHostController,
    vm: ProfileViewModel,
    bookVM: BookViewModel
) {
    val user = vm.currentUser
    val uiState by vm.uiState

    val bookList1 by vm.bookList.observeAsState(emptyList())
    val savedBooks by vm.savedBooks.observeAsState(emptyList())
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars),
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {navController.popBackStack()}
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = DarkBlue
                    )
                }
                Text(
                    text = "Shaxsiy kabinet",
                    color = DarkBlue,
                    fontWeight = FontWeight.W600,
                    fontSize = 24.sp
                )
                IconButton(
                    onClick = {/*todo: navigate settings screen*/}
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = DarkBlue
                    )
                }
            }
        },
    ) {padding->
        if(uiState is UiState.Loading){
            LoadingScreen()
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item{
                ProfileBox(user)
                Spacer(Modifier.height(8.dp))
                BookStats(savedBooks.size)
            }
            item {
                Spacer(Modifier.height(20.dp))
                TitleText(
                    onViewAll = {
                        navController.navigate(Routes.Category.name)
                    },
                    category = null,
                    title = "O'qilayotgan kitoblar")
                Spacer(Modifier.height(8.dp))
                LazyRow(
                    modifier = Modifier.padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(bookList1){book->
                        BookCardHorizontal(
                            book = book,
                            context = context,
                            modifier = Modifier.size(width = 250.dp, height = 120.dp),
                            onClick = {bookId ->
                                navController.navigate("${Routes.Info.name}/${bookId}")
                            }
                        )
                    }
                }
            }
            item {
                Spacer(Modifier.height(12.dp))
                TitleText(
                    onViewAll = {
                        navController.navigate(Routes.Category.name)
                    },
                    category = null,
                    title = "O'qilgan kitoblar")
                LazyRow(
                    modifier = Modifier.padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(bookList1){book->
                        BookCardHorizontal(
                            book = book,
                            context = context,
                            modifier = Modifier.size(width = 250.dp, height = 120.dp),
                            onClick = {bookId ->
                                navController.navigate("${Routes.Info.name}/${bookId}")
                            }
                        )
                    }
                }
            }
            item {
                Spacer(Modifier.height(12.dp))
                TitleText(
                    onViewAll = {
                        navController.navigate(Routes.Category.name)
                    },
                    category = null,
                    title = "Saqlangan kitoblar")
                LazyRow(
                    modifier = Modifier.padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(savedBooks){book->
                        BookCardHorizontal(
                            book = book,
                            context = context,
                            modifier = Modifier.size(width = 250.dp, height = 120.dp),
                            onClick = {bookId ->
                                navController.navigate("${Routes.Info.name}/${bookId}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileBox(
    user: User?
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        user?.let {
            IconButton(
                modifier = Modifier.size(72.dp),
                onClick = {/*todo: change the photo*/},
                colors = IconButtonDefaults.filledIconButtonColors(
                    contentColor = DarkBlue,
                    containerColor = Color(0xFFB1DAEF)
                ),
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                )
            }
            Text(
                text = user.fullname,
                fontWeight = FontWeight.W600,
                fontSize = 24.sp,
                color = DarkBlue)
            Text(
                text = user.username+"@gmail.com",
                fontWeight = FontWeight.W200,
                fontSize = 18.sp,
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun BookStats(
    savedBooks: Int,
){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD9D9D9)
        ),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "2",
                    color = DarkBlue,
                    fontWeight = FontWeight.W600,
                    fontSize = 20.sp
                )
                Text(
                    text = "o'qilayotgan\nkitoblar",
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W500
                )
            }
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "5",
                    color = DarkBlue,
                    fontWeight = FontWeight.W600,
                    fontSize = 20.sp
                )
                Text(
                    text = "o'qilgan\nkitoblar",
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W500
                )
            }
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$savedBooks",
                    color = DarkBlue,
                    fontWeight = FontWeight.W600,
                    fontSize = 20.sp
                )
                Text(
                    text = "saqlangan\nkitoblar",
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W500
                )
            }
        }
    }
}

@Composable
fun BookCardHorizontal(
    book: Book?,
    context: Context,
    modifier: Modifier,
    onClick:(Int) -> Unit
) {
    book?.let {
        Card(
            onClick = {onClick(book.id)},
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = Color.LightGray
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ImageLoader(
                    context = context,
                    imageUrl = book.image,
                    modifier = Modifier
                        .size(width = 60.dp, height = 90.dp)
                        .clip(RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxHeight(),
                ) {
                    Text(
                        text = book.name,
                        fontSize = 16.sp,
                        maxLines = 1,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.W600,
                        color = DarkBlue
                    )
                    Text(
                        text = book.author,
                        fontSize = 12.sp,
                        maxLines = 1,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.W400,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    RatingSection(rating = book.reyting)
                }
            }
        }
    }

}


@Composable
fun RatingSection(rating: Int?) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Star, // Provide your star icon here
            contentDescription = "Star",
            tint = SkyBlue, // Light blue star
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        rating?.let {
            Text(
                text = "$rating.0",
                fontSize = 12.sp,
                fontWeight = FontWeight.W500,
                color = DarkBlue
            )
        }
    }
}