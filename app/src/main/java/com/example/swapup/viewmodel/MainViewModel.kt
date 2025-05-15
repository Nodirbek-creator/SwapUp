package com.example.swapup.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.swapup.data.network.RetrofitInstance
import com.example.swapup.data.repository.AuthRepository
import com.example.swapup.data.repository.FirebaseAuthRepo
import com.example.swapup.data.sharedpref.DataManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MainViewModel(
    private val dataManager: DataManager,
    bookViewModel: BookViewModel,
): ViewModel() {
    val currentUser = dataManager.getUser()
    val selectedCategory = bookViewModel.selectedCategory
    var selectedIndex by mutableIntStateOf(0)
        private set


    fun onIndexChange(newIndex: Int){
        selectedIndex = newIndex
    }

    fun logout(){
        dataManager.removeUser()
    }


}