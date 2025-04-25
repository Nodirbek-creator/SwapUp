package com.example.handybook.navigation

import android.content.SharedPreferences
import android.provider.ContactsContract.Data
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.handybook.db.DataManager
import com.example.handybook.network.ApiService
import com.example.handybook.screens.HomeScreen
import com.example.handybook.screens.LoginScreen
import com.example.handybook.screens.SignUpScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    apiService: ApiService,
    dataManager: DataManager
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable(Routes.Login.name){
            LoginScreen(navController, apiService, dataManager)
        }
        composable(Routes.SignUp.name){
            SignUpScreen(navController, apiService, dataManager)
        }
        composable(Routes.Home.name){
            HomeScreen(navController, apiService, dataManager)
        }
    }
}