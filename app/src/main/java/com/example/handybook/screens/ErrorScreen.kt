package com.example.handybook.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.handybook.ui.theme.DarkBlue

@Composable
fun ErrorScreen(
    icon:Int,
    text: String,
    bgColor: Color = DarkBlue,
    contentColor: Color = Color.White,
) {
    Column(
        modifier = Modifier.fillMaxSize().background(bgColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(96.dp),
            tint = contentColor
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = text,
            fontWeight = FontWeight.W600,
            fontSize = 18.sp,
            color = contentColor,
            textAlign = TextAlign.Center
        )
    }
}