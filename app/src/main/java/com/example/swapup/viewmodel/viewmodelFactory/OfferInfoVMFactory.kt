package com.example.swapup.viewmodel.viewmodelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.swapup.data.repository.FirestoreRepo
import com.example.swapup.data.sharedpref.DataManager
import com.example.swapup.viewmodel.CreateOfferViewModel
import com.example.swapup.viewmodel.DemandInfoViewModel
import com.example.swapup.viewmodel.OfferInfoViewModel

class OfferInfoVMFactory(
    private val dataManager: DataManager,
    private val repo: FirestoreRepo,
    private val uid: String,
    private val context: Context
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(OfferInfoViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return OfferInfoViewModel(dataManager, repo, uid, context) as T
        }
        if(modelClass.isAssignableFrom(DemandInfoViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DemandInfoViewModel(dataManager, repo, uid, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}