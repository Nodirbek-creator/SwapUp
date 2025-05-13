package com.example.swapup.viewmodel.viewmodelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.swapup.data.sharedpref.DataManager
import com.example.swapup.viewmodel.CreateOfferViewModel

class CreateOfferViewModelFactory(
    private val dataManager: DataManager,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateOfferViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateOfferViewModel(dataManager, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}