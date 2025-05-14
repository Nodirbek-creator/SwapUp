package com.example.swapup.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapup.data.model.Demand
import com.example.swapup.data.model.Offer
import com.example.swapup.data.repository.FirestoreRepo
import com.example.swapup.viewmodel.state.UiState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class DemandViewModel(): ViewModel() {
    val repo = FirestoreRepo(FirebaseFirestore.getInstance())


    val demands: StateFlow<List<Demand>> = repo.getDemands()
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
}