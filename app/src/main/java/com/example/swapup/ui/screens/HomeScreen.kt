package com.example.swapup.ui.screens

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.placeholder
import com.example.swapup.R
import com.example.swapup.data.model.Book
import com.example.swapup.ui.theme.DarkBlue
import com.example.swapup.ui.theme.SkyBlue
import com.example.swapup.viewmodel.BookViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.swapup.data.model.Category
import com.example.swapup.navigation.Routes
import com.example.swapup.viewmodel.state.UiState

@Composable
fun HomeScreen(
    navController: NavHostController,
    bookVM: BookViewModel
) {
    val context = LocalContext.current
    val uiState by bookVM.uiState
    val selectedCategory = bookVM.selectedCategory


    val searchQuery = bookVM.searchQuery


    val categoryList by bookVM.categoryList.observeAsState(emptyList())
    val mainBook by bookVM.mainBook.observeAsState()
    val bookList by bookVM.bookList.observeAsState(emptyList())

//    Log.d("uiState","${uiState}")
//    Log.d("selectedCategory","${selectedCategory}")
//    Log.d("searchQuery","${searchQuery}")
//    Log.d("categoryList","${categoryList}")
    Log.d("mainbook","${mainBook?.name}")
    Log.d("bookList","${bookList.size}")


    LaunchedEffect(uiState) {
        if (uiState is UiState.Error) {
            val errorMessage = (uiState as UiState.Error).msg
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    when(uiState){
        is UiState.Loading ->{
            LoadingScreen(
                bgColor = Color.White,
                contentColor = DarkBlue
            )
        }
        is UiState.Success ->{
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(Modifier.height(12.dp))
                Categories(
                    categoryList,
                    onSelected = bookVM::onCategoryChange,
                    selectedCategory
                )
                Spacer(Modifier.height(12.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            value = searchQuery,
                            onValueChange = bookVM::onQueryChange,
                            leadingIcon = {
                                IconButton(
                                    onClick = {navController.navigate(Routes.Search.name)}
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            },
                            placeholder = {Text(stringResource(R.string.search_placeholder), fontWeight = FontWeight.W500, fontSize = 12.sp)},
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedLeadingIconColor = Color.LightGray,
                                unfocusedPlaceholderColor = Color.LightGray,
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = DarkBlue,
                                focusedTextColor = DarkBlue,
                                cursorColor = DarkBlue,
                            )
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                    item{
                        MainBook(
                            book = mainBook!!,
                            context = context,
                            onClick = {bookId->
                                navController.navigate("${Routes.Info.name}/$bookId")
                            }
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                    item{
                        val title = if(selectedCategory.typename == "Barchasi") "Barcha kitoblar" else selectedCategory.typename
                        TitleText(
                            onViewAll = {
                                navController.navigate(Routes.Category.name)
                            },
                            title = title,
                            category = selectedCategory
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                    item{
                        BooksCollection(
                            bookList = bookList,
                            context = context,
                            onBookClick = { bookId->
                                navController.navigate("${Routes.Info.name}/$bookId")
                            }
                        )
                    }
                }
            }
        }
        else->{

        }
    }
}

@Composable
fun Categories(
    categoryList: List<Category>,
    onSelected:(Category) -> Unit,
    selectedCategory: Category,
){
    if(categoryList.isNotEmpty()){
        val scope = rememberCoroutineScope()
        val listState = rememberLazyListState()
        LaunchedEffect(selectedCategory) {
            val index = categoryList.indexOf(selectedCategory)
            if(index != -1){
                listState.animateScrollToItem(index)
            }
        }
        LazyRow(
            state = listState,
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(categoryList){ index, it ->
                Button(
                    onClick = {
                        onSelected(it)
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(it == selectedCategory) DarkBlue else Color.LightGray,
                        contentColor = if(it == selectedCategory) Color.White else Color.Gray
                    ),
                ) {
                    Text(
                        text = it.typename,
                        fontWeight = FontWeight.W500,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MainBook(
    book: Book,
    context: Context,
    onClick: (Int) -> Unit,
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(DarkBlue)
                .padding(horizontal = 20.dp)
                .align(Alignment.BottomStart),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = "${book.author}ning\n“${book.name}”\nasari",
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                color = Color.White
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    onClick(book.id)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = SkyBlue,
                    contentColor = DarkBlue
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(stringResource(R.string.read_now), fontWeight = FontWeight.W500, fontSize = 10.sp)
            }
        }
        ImageLoader(
            context,
            imageUrl = book.image,
            modifier = Modifier
                .size(180.dp, 200.dp)
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp)
        )
    }
}

@Composable
fun TitleText(
    onViewAll:(Category?)-> Unit,
    category: Category?,
    title: String
){
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.W700,
            fontSize = 18.sp,
            color = DarkBlue
        )
        TextButton(
            onClick = {onViewAll(category)},
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = stringResource(R.string.see_all),
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                color = SkyBlue
            )
        }
    }
}

@Composable
fun BooksCollection(
    bookList: List<Book>,
    context: Context,
    onBookClick:(Int) -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {

        if(bookList.isNotEmpty()){
            Log.d(TAG, "BooksCollection: ITSNOTEMPTY")
            val rows = bookList.chunked(2)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
            ) {
                rows.forEachIndexed { index, rowItems ->
                    Log.d(TAG, "BooksCollection: ${index}")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        rowItems.forEach { book->
                            Log.d(TAG, "BooksCollection: ${book.name}")
                            BookCard(
                                book = book,
                                context = context,
                                onClick = {id -> onBookClick(id)}
                            )
                        }
                    }
                }
            }
        }else{
            Log.d(TAG, "BooksCollection: ERRRROR")
            Toast.makeText(context, "ERRORRRR", Toast.LENGTH_SHORT)
        }
    }
}

@Composable
fun BookCard(
    book: Book,
    onClick:(Int) -> Unit,
    context: Context
){
    Card(
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .size((LocalConfiguration.current.screenWidthDp*9/20).dp, height = 300.dp)
            .shadow(12.dp, RoundedCornerShape(5.dp), ambientColor = Color.White),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = {
            onClick(book.id)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(1.dp))
            ImageLoader(
                context,
                imageUrl = book.image,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(220.dp)
            )
            Text(
                text = book.name,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                maxLines = 1,
                fontSize = 12.sp,
                fontWeight = FontWeight.W600,
                color = DarkBlue,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
            Text(
                text = book.author,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.W400,
                color = Color.Gray,
                fontSize = 10.sp,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }
    }
}

fun buildImageRequest(
    context: Context,
    imageUrl: String,
    placeholder: Int,
): ImageRequest {
    return ImageRequest.Builder(context)
        .data(imageUrl)
        .crossfade(true)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .placeholder(placeholder)
        .build()
}

@Composable
fun ImageLoader(
    context: Context,
    imageUrl: String?,
    modifier: Modifier,
    contentScale: ContentScale = ContentScale.Crop
){
    imageUrl?.let {
        AsyncImage(
            model = buildImageRequest(
                context = context,
                imageUrl = imageUrl,
                placeholder = R.drawable.placeholder
            ),
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}
