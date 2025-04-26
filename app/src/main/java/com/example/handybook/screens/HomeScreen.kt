package com.example.handybook.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.handybook.R
import com.example.handybook.db.DataManager
import com.example.handybook.network.ApiService
import com.example.handybook.ui.theme.DarkBlue
import com.example.handybook.ui.theme.SkyBlue

@Composable
fun HomeScreen(
    navController: NavHostController,
    apiService: ApiService,
    dataManager: DataManager,
) {
    val user = dataManager.getUser()
    var searchQuery by rememberSaveable{ mutableStateOf("") }
    val categoryList = listOf("Barchasi", "Badiiy adabiyot", "Psixologiya", "Biznes kitoblar", "Bolalar adabiyoti", "Diniy adabiyotlar", "Siyosat", "Detektiv va fantastika", "Biografiya" )
    var selectedCategory by rememberSaveable { mutableStateOf("Barchasi") }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(12.dp))
        Categories(
            categoryList,
            onSelected = {category->
                selectedCategory = category
            },
            selectedCategory
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {searchQuery = it},
            leadingIcon = {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            placeholder = {Text("Kitob yoki muallifni qidiring...", fontWeight = FontWeight.W500, fontSize = 12.sp)},
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedLeadingIconColor = Color.LightGray,
                unfocusedPlaceholderColor = Color.LightGray,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = DarkBlue,
                focusedTextColor = DarkBlue
            )
        )
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            contentAlignment = Alignment.BottomCenter
        ){
            Column(
                modifier = Modifier.fillMaxWidth().height(120.dp).background(DarkBlue).padding(horizontal = 20.dp).align(Alignment.BottomStart),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ){
                Text(
                    text = "O’tkir Hoshimovning\n“Bahor qaytmaydi”\nasari",
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600,
                    color = Color.White
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {},
                    modifier = Modifier.width(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SkyBlue,
                        contentColor = DarkBlue
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Hoziroq o'qish", fontWeight = FontWeight.W500, fontSize = 10.sp)
                }
            }
            Image(
                painter = painterResource(R.drawable.main_book),
                contentDescription = null,
                modifier = Modifier.size(120.dp).align(Alignment.BottomEnd),

            )
        }

    }
}

@Composable
fun Categories(
    categoryList: List<String>,
    onSelected:(String) -> Unit,
    selectedCategory: String,
){
    LazyRow(
        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categoryList){
            Button(
                onClick = {onSelected(it)},
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if(it == selectedCategory) DarkBlue else Color.LightGray,
                    contentColor = Color.White
                ),
            ) {
                Text(
                    text = it,
                    fontWeight = FontWeight.W500,
                    fontSize = 10.sp
                )
            }
        }
    }
}