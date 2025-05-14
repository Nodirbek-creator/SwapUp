package com.example.swapup.viewmodel.viewmodelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.swapup.data.sharedpref.DataManager
import com.example.swapup.viewmodel.CreateDemandViewModel
import com.example.swapup.viewmodel.CreateOfferViewModel

class CreateViewModelFactory(
    private val dataManager: DataManager,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            CreateOfferViewModel::class.java -> CreateOfferViewModel(dataManager, context) as T
            CreateDemandViewModel::class.java -> CreateDemandViewModel(dataManager, context) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}