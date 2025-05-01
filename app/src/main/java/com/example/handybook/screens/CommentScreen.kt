package com.example.handybook.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.handybook.R
import com.example.handybook.navigation.Routes
import com.example.handybook.ui.theme.DarkBlue
import com.example.handybook.ui.theme.SkyBlue

@Composable
fun CommentScreen(
    navController: NavHostController
){
    var comment by remember {
        mutableStateOf("")
    }
    var rating by remember {
        mutableIntStateOf(0)
    }

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
                        navController.navigate(Routes.Info.name)
                    }
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        "Back",
                        tint = Color.White
                    )
                }
                Text("O’z sharhingizni \n" +
                        "yozib qoldiring", fontSize = 22.sp, color = Color.White, textAlign = TextAlign.Center)
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painterResource(R.drawable.saved),
                        "Favourite",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        floatingActionButton = {
            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(DarkBlue),
                    shape = RoundedCornerShape(9.dp),
                    modifier = Modifier.size(196.dp,48.dp)
                ) {
                    Text("Jo'natish")
                }
                TextButton(
                    onClick = {}
                ) {
                    Text("Bekor qilish", color = DarkBlue)
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ){paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues),
            contentAlignment = Alignment.Center
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
                        .fillMaxHeight(0.27f)
                        .background(DarkBlue),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text("“O’tkan kunlar” romani sizga qanchalik manzur keldi?", fontSize = 18.sp, color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(0.75f), fontWeight = FontWeight.Light)
                }
                Spacer(modifier = Modifier.height(64.dp))
                Text(
                    "Kitobni baholang",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = DarkBlue
                )
                Row {
                    for (i in 1..5){
                        IconButton(
                            onClick = {
                                rating = i
                            },
                            modifier = Modifier.size(48.dp)
                        ){
                            Icon(
                                Icons.Rounded.Star, "Rating", modifier = Modifier.size(64.dp),
                                tint = if (i <= rating) SkyBlue else Color.LightGray
                            )
                        }
                    }
                }
                    Text("Kitob haqida o’z fikringizni yozib qoldiring", fontSize = 14.sp, color = DarkBlue, modifier = Modifier.fillMaxWidth(0.9f), textAlign = TextAlign.Center)
                OutlinedTextField(
                    value = comment,
                    onValueChange = {
                        comment = it
                    },
                    textStyle = TextStyle(color = DarkBlue),
                    placeholder = {
                        Text("Mening ushbu kitob haqida fikrim...")
                    },
                    shape = RoundedCornerShape(9.dp),
                    modifier = Modifier.fillMaxWidth(0.9f).height(164.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.LightGray,
                        focusedIndicatorColor = Color.LightGray,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(128.dp))
            }
            Box(
                modifier = Modifier
                    .offset(y = (-LocalConfiguration.current.screenHeightDp / 4.5).dp)
                    .size((LocalConfiguration.current.screenHeightDp/4.5).dp)
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFFEB3B),
                                Color(0xFFFF9800)
                            )
                        )
                    )
            ){
                Icon(
                    painterResource(
                        when(rating){
                            1 -> R.drawable.baseline_sentiment_very_dissatisfied_24
                            2 -> R.drawable.baseline_sentiment_dissatisfied_24
                            3 -> R.drawable.baseline_sentiment_neutral_24
                            4 -> R.drawable.baseline_sentiment_satisfied_24
                            5 -> R.drawable.baseline_sentiment_very_satisfied_24
                            else -> {
                                R.drawable.baseline_sentiment_neutral_24
                            }
                        }
                    ),
                    "emoji",
                    modifier = Modifier.size((LocalConfiguration.current.screenHeightDp/4.5).dp),
                    tint = Color.DarkGray
                )
            }
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