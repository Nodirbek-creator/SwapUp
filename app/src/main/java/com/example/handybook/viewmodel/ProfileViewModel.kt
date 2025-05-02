package com.example.handybook.viewmodel

import androidx.lifecycle.ViewModel
import com.example.handybook.data.repository.AuthRepository
import com.example.handybook.data.sharedpref.DataManager

class ProfileViewModel(private val dataManager: DataManager): ViewModel() {
    val currentUser = dataManager.getUser()
}