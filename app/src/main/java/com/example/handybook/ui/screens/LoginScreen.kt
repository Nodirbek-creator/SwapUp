package com.example.handybook.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.handybook.R
import com.example.handybook.navigation.Routes
import com.example.handybook.viewmodel.state.UiState
import com.example.handybook.ui.theme.DarkBlue
import com.example.handybook.ui.theme.SkyBlue
import com.example.handybook.viewmodel.LoginViewModel
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    navController: NavHostController,
    vm: LoginViewModel
) {
    //txtField vars
    val username = vm.username
    val password = vm.password
    val passwordVisible = vm.passwordVisible
    val uiState by vm.uiState

    val context = LocalContext.current


//    LaunchedEffect(uiState) {
//        if (uiState is UiState.Error) {
//            val errorMessage = (uiState as UiState.Error).msg
//            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
//        }
//    }
    when(uiState){
        is UiState.Error ->{
            ErrorDialog(
                errorMessage = (uiState as UiState.Error).msg,
                onResetUiState = {vm.resetUiState()}
            )
        }
        is UiState.Loading ->{
            LoadingScreen()
        }
        is UiState.Success ->{
            navController.navigate(Routes.Main.name)
        }
        else ->{}
    }
    Column(
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(Modifier.height(8.dp))
        Image(
            painter = painterResource(R.drawable.logo_dark),
            contentDescription = null,
            modifier = Modifier.size(160.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(32.dp))
        Text(
            text = "Hisobingizga kiring",
            fontSize = 20.sp,
            fontWeight = FontWeight.W700,
            color = DarkBlue,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            textAlign = TextAlign.Start
        )
        Spacer(Modifier.height(20.dp))
        Column(
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp),
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
                placeholder = { Text("@your_username") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedBorderColor = DarkBlue
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                supportingText = {
                    if(vm.usernameError){
                        Text("Invalid format.\n${stringResource(R.string.username_format)}")
                    }
                },
                isError = vm.usernameError,
                keyboardActions = KeyboardActions(
                    onDone = {
                        vm.usernameValid(username)
                    }
                )
            )
        }
        Spacer(Modifier.height(12.dp))
        Column(
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "Password",
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                color = DarkBlue,
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = vm::onPasswordChange,
                placeholder = { Text("12345678") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedBorderColor = DarkBlue
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                isError = vm.passwordError,
                keyboardActions = KeyboardActions(
                    onDone = {
                        vm.passwordValid(password)
                    }
                ),
                supportingText = {
                    if(vm.passwordError){
                        Text("Invalid format.\n${stringResource(R.string.password_format)}")
                    }
                },
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
        Spacer(Modifier.height(128.dp))
        Button(
            onClick = {
                vm.login()
            },
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SkyBlue,
                contentColor = DarkBlue
            ),
        ) {
            Text(
                text = "Kirish",
                fontWeight = FontWeight.W500,
                fontSize = 15.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hisobingiz yo'qmi? ",
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                color = DarkBlue
            )
            TextButton(
                onClick = {navController.navigate(Routes.SignUp.name)},
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Ro'yxatdan o'ting",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = SkyBlue
                )
            }
        }
    }



}

@Composable
fun ErrorDialog(errorMessage: String,
                onResetUiState: () -> Unit){
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "",
                tint = DarkBlue
            )
        },
        title = {
            Text(
                textAlign = TextAlign.Center,
                text = errorMessage,
                fontSize = 14.sp
            )
        },
        onDismissRequest = {
            onResetUiState()
        },
        confirmButton = {
            Button(
                onClick = {onResetUiState()},
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkBlue,
                    contentColor = Color.White,
                ),
                shape = RoundedCornerShape(4.dp),
            ) {
                Text(
                    text = "OK"
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {onResetUiState()},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = DarkBlue,
                ),
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, DarkBlue)
            ) {
                Text(
                    text = "Cancel"
                )
            }
        }
    )
}

@Composable
fun LoadingScreen(
    bgColor: Color = Color.Transparent,
    contentColor: Color = DarkBlue
){
    Column(
        modifier = Modifier.fillMaxSize().background(bgColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = contentColor)
    }
}
@Composable
fun SuccessScreen(
    navigateTo:()-> Unit
){
    LaunchedEffect(Unit) {
        delay(500)
        navigateTo()
    }
    Column(
        modifier = Modifier.fillMaxSize().background(DarkBlue),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .border(2.dp,Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ){
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
        }
//        Text(
//            text = "Iltimos kutib turing",
//            fontWeight = FontWeight.W300,
//            fontSize = 16.sp,
//            color = Color.White
//        )
    }
}
