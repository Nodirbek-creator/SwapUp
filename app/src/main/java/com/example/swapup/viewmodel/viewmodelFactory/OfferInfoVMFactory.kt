package com.example.swapup.viewmodel.viewmodelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.swapup.data.repository.FirestoreRepo
import com.example.swapup.data.sharedpref.DataManager
import com.example.swapup.viewmodel.CreateOfferViewModel
import com.example.swapup.viewmodel.OfferInfoViewModel

class OfferInfoVMFactory(
    private val dataManager: DataManager,
    private val repo: FirestoreRepo,
    private val uid: String
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(OfferInfoViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return OfferInfoViewModel(dataManager, repo, uid) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}