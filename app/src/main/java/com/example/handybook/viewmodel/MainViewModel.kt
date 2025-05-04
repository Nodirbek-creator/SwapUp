package com.example.handybook.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.handybook.data.network.RetrofitInstance
import com.example.handybook.data.repository.AuthRepository
import com.example.handybook.data.sharedpref.DataManager
import kotlinx.coroutines.launch

class MainViewModel(
    private val dataManager: DataManager,
    private val navController: NavHostController
): ViewModel() {
    val currentUser = dataManager.getUser()
    val authRepository = AuthRepository(RetrofitInstance.api, dataManager)

    var selectedIndex by mutableIntStateOf(0)
        private set


    fun onIndexChange(newIndex: Int){
        selectedIndex = newIndex
    }

    fun logout(){
        viewModelScope.launch {
            authRepository.logout()
        }
    }


}