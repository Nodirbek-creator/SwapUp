package com.example.swapup.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapup.data.model.Book
import com.example.swapup.data.network.RetrofitInstance
import com.example.swapup.data.repository.BookRepository
import com.example.swapup.data.sharedpref.DataManager
import com.example.swapup.viewmodel.state.UiState
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val dataManager: DataManager): ViewModel() {
    private val repository = BookRepository(RetrofitInstance.api)
    val currentUser = dataManager.getUser()

    private val _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState:State<UiState> get() = _uiState

    private val _bookList = MutableLiveData<List<Book>>(emptyList())
    val bookList: LiveData<List<Book>> get() = _bookList

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
            val response = repository.getAllBooks()
            if(response.isSuccessful){
                _bookList.value = response.body()
                idle()
            }
            else{
                error(response.message())
            }
        }
    }
}