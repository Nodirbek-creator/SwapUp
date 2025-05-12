package com.example.swapup.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swapup.data.model.Offer
import com.example.swapup.data.repository.FirestoreRepo
import com.example.swapup.viewmodel.state.UiState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OfferViewModel(): ViewModel() {
    val repo = FirestoreRepo(FirebaseFirestore.getInstance())

    private val _offerList = MutableLiveData<List<Offer>>()
    val offerList get() = _offerList

    val offers: StateFlow<List<Offer>> = repo.getOffers()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    private val _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState: State<UiState> get() = _uiState

    fun loading(){
        _uiState.value = UiState.Loading
    }
    fun success(){
        _uiState.value = UiState.Success
    }
    fun idle(){
        _uiState.value = UiState.Idle
    }
    fun error(msg: String){
        _uiState.value = UiState.Error(msg)
    }

//    fun getAllOffers(){
//        viewModelScope.launch {
//            loading()
//            val offerList = repo.getOffers()
//            Log.d("OfferVM","${offerList}")
//            if(offerList.isNotEmpty()){
//                _offerList.value = offerList
//                success()
//            }
//            else{
//                error("Failed to load offers")
//            }
//        }
//    }
}