package com.example.handybook.screens

import android.content.SharedPreferences
import android.util.Log
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.handybook.dataclass.SignUp
import com.example.handybook.dataclass.User
import com.example.handybook.db.DataManager
import com.example.handybook.navigation.Routes
import com.example.handybook.network.ApiService
import com.example.handybook.ui.theme.DarkBlue
import com.example.handybook.ui.theme.SkyBlue
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun SignUpScreen(
    navController: NavHostController,
    apiService: ApiService,
    dataManager: DataManager
) {
    //txtField vars
    var username by rememberSaveable { mutableStateOf("") }
    var fullname by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(Modifier.height(8.dp))
        Image(
            painter = painterResource(R.drawable.logo_dark),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(32.dp))
        Text(
            text = "Ro'yhatdan o'ting",
            fontSize = 20.sp,
            fontWeight = FontWeight.W700,
            color = DarkBlue,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            textAlign = TextAlign.Start
        )
        Spacer(Modifier.height(20.dp))
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
                onValueChange = {username = it},
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
                text = "Fullname",
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                color = DarkBlue,
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = fullname,
                onValueChange = {fullname = it},
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
                onValueChange = {email = it},
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
                text = "Password",
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                color = DarkBlue,
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = {password = it},
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
                        onClick = {passwordVisible = !passwordVisible}
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,)
                    }
                },
                visualTransformation = if(!passwordVisible) PasswordVisualTransformation() else VisualTransformation.None
            )
        }
        Spacer(Modifier.height(48.dp))
        Button(
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
            onClick = {
                apiService.signUp(SignUp(username,fullname,email,password)).enqueue(object: Callback<User> {
                    override fun onResponse(
                        call: Call<User>,
                        response: Response<User>
                    ) {
                        if(response.isSuccessful){
                            val user = response.body()
                            dataManager.saveUser(user!!)
                            navController.navigate(Routes.Home.name)
                        }
                    }

                    override fun onFailure(
                        call: Call<User>,
                        t: Throwable) {
                        Log.d("onFailure", "${t.localizedMessage}")
                        Log.d("onFailure", "${t.cause}")
                    }

                })
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SkyBlue,
                contentColor = DarkBlue
            )
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