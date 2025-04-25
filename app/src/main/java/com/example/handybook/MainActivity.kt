package com.example.handybook

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.handybook.db.DataManager
import com.example.handybook.navigation.AppNavHost
import com.example.handybook.navigation.Routes
import com.example.handybook.network.ApiService
import com.example.handybook.network.RetrofitBuilder
import com.example.handybook.ui.theme.HandybookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val apiService = RetrofitBuilder.getInstance().create(ApiService::class.java)
        val sharedPreference = this@MainActivity.getPreferences(Context.MODE_PRIVATE)
        val dataManager = DataManager(sharedPreference)
        setContent {
            val navController = rememberNavController()
            AppNavHost(
                navController,
                startDestination = Routes.Login.name,
                apiService,
                dataManager)
        }
    }
}
