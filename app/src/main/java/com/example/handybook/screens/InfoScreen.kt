package com.example.handybook.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.handybook.R
import com.example.handybook.data.model.Book
import com.example.handybook.navigation.Routes
import com.example.handybook.ui.theme.DarkBlue
import com.example.handybook.ui.theme.SkyBlue

@Composable
fun InfoScreen(
    navController: NavHostController
) {
    var bookType by remember {
        mutableStateOf("E-Book")
    }
    var bookInfo by remember {
        mutableStateOf("Sharh")
    }
    val loremIpsum by remember {
        mutableStateOf("Modomiki, biz yangi davrga oyoq qo‘ydik, bas, biz har bir yo‘sunda ham shu yangi davrning yangiliklari ketidan ergashamiz va shunga o‘xshash dostonchiliq, ro‘monchiliq va hikoyachiliqlarda ham yangarishg‘a, xalqimizni shu zamonning «Tohir-Zuhra»lari, «Chor darvesh»lari, «Farhod-Shirin» va «Bahromgo‘r»lari bilan tanishdirishka o‘zimizda majburiyat his etamiz. Yozmoqqa niyatlanganim ushbu — «O‘tkan kunlar», yangi zamon ro‘monchilig‘i bilan tanishish yo‘lida kichkina bir tajriba, yana to‘g‘risi, bir havasdir. Ma’lumki, har bir ishning ham yangi — ibtidoiy davrida talay kamchilik-lar bilan maydong‘a chiqishi, ahllarining yetishmaklari ila sekin-sekin tuzalib, takomulga yuz tutishi tabiiy bir holdir. Mana shuning daldasida havasimda jasorat etdim, havaskorlik orqasida kechaturgan qusur va xatolardan cho‘chib turmadim. Moziyg‘a qaytib ish ko‘rish xayrlik, deydilar. Shunga ko‘ra mavzu’ni moziydan, yaqin o‘tkan kunlardan, tari-ximizning eng kirlik, qora kunlari bo‘lg‘an keyingi «xon zamonlari»dan belguladim.")
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
                    }
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        "Back",
                        tint = Color.White
                    )
                }
                Text("Batafsil", fontSize = 22.sp, color = Color.White)
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
            if(bookInfo == "Tavsif"){
                if (bookType == "E-Book") {
                    Card(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        onClick = {
                            /*TODO: OPEN BOOK PDF*/
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .weight(8f)
                                    .background(DarkBlue)
                                    .height(54.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("O'qishni davom ettirish", color = SkyBlue, fontSize = 18.sp)
                            }
                            Row(
                                modifier = Modifier
                                    .weight(2f)
                                    .background(SkyBlue)
                                    .height(54.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("87%", color = DarkBlue, fontSize = 18.sp)
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
                            Text("4.0", color = DarkBlue, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("52 ta sharhlar", color = Color.LightGray, fontSize = 6.sp, modifier = Modifier.offset(y = (-4).dp))
                        }
                        Row {
                            for(i in 1..4){
                                Icon(Icons.Default.Star, "Rating", tint = SkyBlue, modifier = Modifier.size(16.dp))
                            }
                            for(i in 1..1){
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
        }
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
                        .padding(bottom = if(bookInfo != "Iqtibos") 74.dp else 0.dp)
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
                                                    bookType = "E-Book"
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
                                                    bookType = "AudioBook"
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
                                    "O'tkan kunlar",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.W600,
                                    color = DarkBlue
                                )
                                Text("Abdulla Qodiriy", color = Color.LightGray)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Star, "Rating", tint = SkyBlue)
                                    Text("5.0", color = DarkBlue)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(0.95f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    TextButton(
                                        onClick = {
                                            bookInfo = "Tavsif"
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
                                            bookInfo = "Sharh"
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
                                            bookInfo = "Iqtibos"
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
                            if (bookType == "E-Book") {
                                Image(
                                    painter = painterResource(R.drawable.kitob),
                                    contentDescription = "Kitob",
                                    modifier = Modifier
                                        .size((LocalConfiguration.current.screenHeightDp / 3.75).dp)
                                        .offset(y = (-24).dp)
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size((LocalConfiguration.current.screenHeightDp / 4.5).dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.kitob),
                                        contentDescription = "Kitob",
                                        modifier = Modifier
                                            .size((LocalConfiguration.current.screenHeightDp / 4.5).dp),
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
                                    Text("209 bet", color = Color.LightGray, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Icon(
                                        painterResource(R.drawable.outline_headphones_24),
                                        "vaqt",
                                        tint = Color.LightGray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text("12 soat", color = Color.LightGray, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Icon(
                                        painterResource(R.drawable.outline_language_24),
                                        "til",
                                        tint = Color.LightGray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        "O'zbek tilida",
                                        color = Color.LightGray,
                                        fontSize = 12.sp,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1
                                    )
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    loremIpsum,
                                    color = DarkBlue,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.graphicsLayer { alpha = 0.99F })
                                Spacer(modifier = Modifier.size(24.dp))
                            }
                        }
                    }else if(
                        bookInfo == "Sharh"
                    ){
                        items(7){
                            Spacer(modifier = Modifier.height(16.dp))
                            CommentCard()
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
fun CommentCard(){
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
                        Text("User123", color = DarkBlue, fontSize = 16.sp)
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
            Text("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", color = DarkBlue)
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