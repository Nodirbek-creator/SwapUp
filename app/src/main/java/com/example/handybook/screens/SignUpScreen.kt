package com.example.handybook.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.handybook.R
import com.example.handybook.data.model.SignUp
import com.example.handybook.data.model.User
import com.example.handybook.data.sharedpref.DataManager
import com.example.handybook.navigation.Routes
import com.example.handybook.data.network.ApiService
import com.example.handybook.state.UiState
import com.example.handybook.ui.theme.DarkBlue
import com.example.handybook.ui.theme.SkyBlue
import com.example.handybook.viewmodel.SignUpViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun SignUpScreen(
    navController: NavHostController,
    vm: SignUpViewModel
) {
    val uiState by vm.uiState
    //txtField vars
    val username = vm.username
    var fullname = vm.fullname
    var email = vm.email
    var password = vm.password

    var passwordVisible = vm.passwordVisible

    val context = LocalContext.current
//    LaunchedEffect(uiState) {
//        if (uiState is UiState.Error) {
//
//        }
//    }
    when(uiState){
        is UiState.Idle->{}
        is UiState.Loading->{
            LoadingScreen()
        }
        is UiState.Success-> {
            navController.navigate(Routes.Main.name)
        }
        is UiState.Error-> {
            val errorMessage = (uiState as UiState.Error).msg
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {navController.navigate(Routes.Login.name)}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = DarkBlue
                )
            }
            Text(
                text = "Ro'yhatdan o'tish",
                fontSize = 20.sp,
                fontWeight = FontWeight.W700,
                color = DarkBlue,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                textAlign = TextAlign.Start
            )
            Spacer(Modifier.width(12.dp))
        }
        Spacer(Modifier.height(32.dp))
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Username",
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                color = DarkBlue,
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = username,
                onValueChange = vm::onUsernameChange,
                placeholder = { Text("admin123") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedBorderColor = DarkBlue,
                    focusedTextColor = DarkBlue,
                )
            )
        }
        Spacer(Modifier.height(12.dp))
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Familiya va Ism",
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                color = DarkBlue,
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = fullname,
                onValueChange = vm::onFullnameChange,
                placeholder = { Text("Ali Valiyev") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedBorderColor = DarkBlue,
                    focusedTextColor = DarkBlue,
                )
            )
        }
        Spacer(Modifier.height(12.dp))
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Email",
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                color = DarkBlue,
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = vm::onEmailChange,
                placeholder = { Text("ali@gmail.com") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedBorderColor = DarkBlue,
                    focusedTextColor = DarkBlue,
                )
            )
        }
        Spacer(Modifier.height(12.dp))
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Parol",
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                color = DarkBlue,
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = vm::onPasswordChange,
                placeholder = { Text("12345") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedBorderColor = DarkBlue,
                    focusedTextColor = DarkBlue
                ),
                trailingIcon = {
                    IconButton(
                        onClick = vm::toggleVisibility
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,)
                    }
                },
                visualTransformation = if(!passwordVisible) PasswordVisualTransformation() else VisualTransformation.None
            )
        }
        Spacer(Modifier.height(64.dp))
        Button(
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
            onClick = {
                vm.signUp()
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SkyBlue,
                contentColor = DarkBlue
            )
        ) {
            Text(
                text = "Ro'yhatdan o'tish",
                fontWeight = FontWeight.W500,
                fontSize = 15.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hisobingiz bormi? ",
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                color = DarkBlue
            )
            TextButton(
                onClick = {navController.navigate(Routes.Login.name)},
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Hisobingiza kiring",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = SkyBlue
                )
            }
        }
    }
}