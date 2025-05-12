package com.example.swapup.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.example.swapup.R
import com.example.swapup.data.model.Offer
import com.example.swapup.ui.theme.DarkBlue
import com.example.swapup.ui.theme.SkyBlue
import com.example.swapup.viewmodel.OfferViewModel

@Composable
fun OfferScreen(
    navController: NavHostController,
    vm: OfferViewModel
) {
    val list by vm.offers.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Spacer(Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(list){offer->
                OfferCard(offer)
                Log.d("isOfferActive?","${offer.active}")
            }
        }
    }
}

@Composable
fun OfferCard(offer: Offer){
    val context = LocalContext.current
    var bitmap: Bitmap? = null
    try {
        val base64Image = Base64.decode(offer.photo, Base64.DEFAULT)
        bitmap = BitmapFactory.decodeByteArray(
            base64Image, 0 ,
            base64Image.size
        )
    } catch (e: Exception){ }
    Card(
        onClick = {},
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ){
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp)),){
                AsyncImage(
                    model = imageRequest(
                        context,
                        bitmap,
                        R.drawable.placeholder
                    ),
                    contentDescription = "null",
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)).align(Alignment.Center),
                    contentScale = ContentScale.Crop,
                )
                Card(
                    modifier = Modifier.padding(4.dp).size(64.dp, 32.dp).align(Alignment.TopStart),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if(offer.active) "Available" else "Reserved",
                            fontWeight = FontWeight.W500,
                            fontSize = 14.sp,
                            color = DarkBlue)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Column(
                modifier = Modifier.fillMaxWidth().padding(start = 4.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = offer.title,
                    fontWeight = FontWeight.W600,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = DarkBlue
                )
                Text(
                    text = offer.author,
                    fontWeight = FontWeight.W400,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray
                )
                Text(
                    text = "Publisher:",
                    fontWeight = FontWeight.W600,
                    fontSize = 14.sp,
                    color = DarkBlue
                )
                Text(
                    text = offer.publisher,
                    fontWeight = FontWeight.W600,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = SkyBlue
                )
            }
        }
    }
}
fun imageRequest(
    context: Context,
    imageUrl: Bitmap?,
    placeholder: Int,
): ImageRequest {
    return ImageRequest.Builder(context)
        .data(imageUrl)
        .crossfade(true)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .error(placeholder)
        .placeholder(placeholder)
        .build()
}
//        val list = listOf(
//            Offer(
//                uid = "",
//                title = "Laser WorkBook",
//                author = "Malcolm Mann",
//                language = Language.English,
//                photo = "",
//                description = "Basic english book for beginners",
//                isActive = true,
//                publisher = "@NodirbekBakhromov1"
//            ),
//            Offer(
//                uid = "",
//                title = "Laser WorkBook",
//                author = "Malcolm Mann",
//                language = Language.English,
//                photo = "${R.drawable.kitob}",
//                description = "Basic english book for beginners",
//                isActive = false,
//                publisher = "@NodirbekBakhromov1"
//            ),
//            Offer(
//                uid = "",
//                title = "Laser WorkBook",
//                author = "Malcolm Mann",
//                language = Language.English,
//                photo = "${R.drawable.kitob}",
//                description = "Basic english book for beginners",
//                isActive = true,
//                publisher = "@NodirbekBakhromov1"
//            ),
//            Offer(
//                uid = "",
//                title = "Laser WorkBook",
//                author = "Malcolm Mann",
//                language = Language.English,
//                photo = "",
//                description = "Basic english book for beginners",
//                isActive = false,
//                publisher = "@NodirbekBakhromov1"
//            )
//        )
