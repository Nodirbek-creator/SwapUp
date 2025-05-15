package com.example.swapup.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapup.data.model.Book
import com.example.swapup.data.model.Demand
import com.example.swapup.data.model.Offer
import com.example.swapup.data.network.RetrofitInstance
import com.example.swapup.data.repository.BookRepository
import com.example.swapup.data.repository.FirestoreRepo
import com.example.swapup.data.sharedpref.DataManager
import com.example.swapup.viewmodel.state.UiState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val dataManager: DataManager): ViewModel() {
    private val bookRepository = BookRepository(RetrofitInstance.api)
    private val firebaseRepo = FirestoreRepo(Firebase.firestore)
    val currentUser = dataManager.getUser()

    val offers: StateFlow<List<Offer>> = firebaseRepo.getOffersByUsername(currentUser.username)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )
    val demands: StateFlow<List<Demand>> = firebaseRepo.getDemandsByUsername(currentUser.username)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    private val _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState:State<UiState> get() = _uiState

    private val _bookList = MutableLiveData<List<Book>>(emptyList())
    val bookList: LiveData<List<Book>> get() = _bookList

    private val _savedBooks = MutableLiveData<List<Book>>(emptyList())
    val savedBooks: LiveData<List<Book>> get() = _savedBooks


    init {
        loadInitialData()
    }
    fun loading(){
        _uiState.value = UiState.Loading
    }
    fun success(){
        _uiState.value = UiState.Success
    }
    fun error(msg: String){
        _uiState.value = UiState.Error(msg)
    }
    fun idle(){
        _uiState.value = UiState.Idle
    }

    fun loadInitialData(){
        viewModelScope.launch {
            loading()
            val savedResponse = firebaseRepo.getSavedBooks(currentUser)
            if(savedResponse.isNotEmpty()){
                _savedBooks.value = savedResponse
                idle()
            }
        }
    }
}