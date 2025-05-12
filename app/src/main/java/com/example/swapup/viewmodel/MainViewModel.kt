package com.example.swapup.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.swapup.data.network.RetrofitInstance
import com.example.swapup.data.repository.AuthRepository
import com.example.swapup.data.sharedpref.DataManager
import kotlinx.coroutines.launch

class MainViewModel(
    private val dataManager: DataManager,
    bookViewModel: BookViewModel,
    private val navController: NavHostController
): ViewModel() {
    val currentUser = dataManager.getUser()
    val selectedCategory = bookViewModel.selectedCategory
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