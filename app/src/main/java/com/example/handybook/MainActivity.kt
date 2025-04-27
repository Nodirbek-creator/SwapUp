package com.example.handybook

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.handybook.navigation.Routes
import com.example.handybook.screens.HomeScreen
import com.example.handybook.screens.LoginScreen
import com.example.handybook.screens.MainScreen
import com.example.handybook.screens.SignUpScreen
import com.example.handybook.viewmodel.AuthViewModel
import com.example.handybook.viewmodel.BookViewModel
import com.example.handybook.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val authViewModel = AuthViewModel()
            val mainViewModel = MainViewModel(authViewModel, navController)
            val bookViewModel = BookViewModel()
            NavHost(
                navController = navController,
                startDestination = Routes.Login.name
            ){
                composable(Routes.Login.name){
                    LoginScreen(navController, authViewModel )
                }
                composable(Routes.SignUp.name){
                    SignUpScreen(navController, authViewModel)
                }
                navigation(startDestination = Routes.Home.name, route = Routes.Main.name){
                    composable(Routes.Home.name){
                        MainScreen(
                            navController = navController,
                            vm = mainViewModel,
                            content = {
                                HomeScreen(
                                    navController = navController,
                                    bookVM = bookViewModel,
                                )
                            }
                        )
                    }
                    composable(Routes.Search.name){
                        MainScreen(
                            navController = navController,
                            vm = mainViewModel,
                            content = {

                            }
                        )
                    }
                    composable(Routes.Articles.name){
                        MainScreen(
                            navController = navController,
                            vm = mainViewModel,
                            content = {
                            }
                        )
                    }
                    composable(Routes.Saved.name){
                        MainScreen(
                            navController = navController,
                            vm = mainViewModel,
                            content = {

                            }
                        )
                    }
                    composable(Routes.Settings.name){
                        MainScreen(
                            navController = navController,
                            vm = mainViewModel,
                            content = {

                            }
                        )
                    }
                }

            }
        }
    }
}
