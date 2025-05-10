package com.example.swapup.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.swapup.R
import com.example.swapup.navigation.Routes
import com.example.swapup.ui.theme.DarkBlue
import com.example.swapup.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    navController: NavHostController,
    vm: SearchViewModel,
){
    vm.getHistory()
    val searchQuery by vm.searchQuery.collectAsState()
    val bookList by vm.bookList.collectAsState()
    val searchHistory by vm.searchHistory
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = searchQuery,
            onValueChange = vm::updateQuery,
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
            placeholder = { Text(stringResource(R.string.search_placeholder), fontWeight = FontWeight.W500, fontSize = 12.sp) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedLeadingIconColor = Color.LightGray,
                unfocusedPlaceholderColor = Color.LightGray,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = DarkBlue,
                focusedTextColor = DarkBlue,
                cursorColor = DarkBlue,
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { focusManager.clearFocus() }
            )
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                if(
                    searchHistory != ""
                ){
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        stringResource(R.string.search_history),
                        fontSize = 24.sp,
                        modifier = Modifier.fillMaxWidth(0.85f),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyHorizontalGrid(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        rows = GridCells.Fixed(2)
                    ) {
                        items(1){
                            Spacer(Modifier.width(16.dp))
                        }
                        items(searchHistory.split(":").reversed()) {bookId ->
                            if(
                                bookId != ""
                            ){
                                BookCardHorizontal(
                                    book = bookList.find { it.id == bookId.toInt() },
                                    context = context,
                                    modifier = Modifier.size(width = 250.dp, height = 120.dp)
                                ) {
                                    vm.writeHistory(bookId.toInt())
                                }
                            }
                        }
                        items(2){
                            Spacer(Modifier.width(16.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                }
            }
            item{
                Box(
                    modifier = Modifier.padding(horizontal = 8.dp)
                ){
                    BooksCollection(
                        bookList = bookList,
                        context = context
                    ) { bookId ->
                        vm.writeHistory(bookId)
                    }
                }
            }
        }
    }
}