package com.example.handybook.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.handybook.navigation.Screen
import com.example.handybook.screens.getCurrentRoute

class MainViewModel(
    private val authVM: AuthViewModel,
    private val navController: NavHostController
): ViewModel() {
    val currentUser = authVM.currentUser

    var selectedIndex by mutableIntStateOf(0)
        private set


    fun onIndexChange(newIndex: Int){
        selectedIndex = newIndex
    }

    fun navigateToScreen(route: String){
        when(route){
            "Teleram" -> { TODO("go to the TG channel") }
            "Share" ->   { TODO("share the link") }
            else -> {
                navController.navigate(route)
            }
        }
    }
}