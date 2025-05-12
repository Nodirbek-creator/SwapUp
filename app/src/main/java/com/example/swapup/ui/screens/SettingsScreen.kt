package com.example.swapup.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swapup.R
import com.example.swapup.ui.theme.DarkBlue

@Composable
fun SettingsScreen(
    localLanguage: String,
    onEnglishClick:() -> Unit,
    onUzbekClick:()-> Unit,
    onRussianClick:() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.choose_language),
            fontSize = 20.sp,
            fontWeight = FontWeight.W600,
            color = DarkBlue,
        )
        Button(
            onClick = {onEnglishClick()},
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, DarkBlue)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.eng),
                        contentDescription = "eng",
                        modifier = Modifier.size(32.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = stringResource(R.string.english),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W500,
                        color = DarkBlue
                    )
                }
                if(localLanguage == "en"){
                    Icon(
                        imageVector = Icons.Default.Check,
                        tint = Color(0xFF4CAF50),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
        Button(
            onClick = {onUzbekClick()},
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, DarkBlue)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.uzb),
                        contentDescription = "uzb",
                        modifier = Modifier.size(32.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = stringResource(R.string.uzbek),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W500,
                        color = DarkBlue
                    )
                }
                if(localLanguage == "uz"){
                    Icon(
                        imageVector = Icons.Default.Check,
                        tint = Color(0xFF4CAF50),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
        Button(
            onClick = {onRussianClick()},
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, DarkBlue)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ru),
                        contentDescription = "ru",
                        modifier = Modifier.size(32.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = stringResource(R.string.russian),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W500,
                        color = DarkBlue
                    )
                }
                if(localLanguage == "ru"){
                    Icon(
                        imageVector = Icons.Default.Check,
                        tint = Color(0xFF4CAF50),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}