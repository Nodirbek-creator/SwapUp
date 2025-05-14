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
import com.example.swapup.data.repository.FirestoreRepo
import com.example.swapup.data.sharedpref.DataManager
import com.example.swapup.viewmodel.state.UiState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val dataManager: DataManager
): ViewModel() {


    private val bookRepository = BookRepository(RetrofitInstance.api)
    private val firebaseRepo = FirestoreRepo(Firebase.firestore)
    val currentUser = dataManager.getUser()

    private val _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState:State<UiState> get() = _uiState

    private val _bookList = MutableLiveData<List<Book>>(emptyList())
    val bookList: LiveData<List<Book>> get() = _bookList

    private val _savedBooks = MutableLiveData<List<Book>>(emptyList())
    val savedBooks: LiveData<List<Book>> get() = _savedBooks

    val readingBooks = mutableListOf<Book?>(null)

    val finishedBooks = mutableListOf<Book?>(null)

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
            val bookResponse = bookRepository.getAllBooks()
            val savedResponse = firebaseRepo.getSavedBooks(currentUser)
            if(bookResponse.isSuccessful){
                _bookList.value = bookResponse.body()
                _savedBooks.value = savedResponse
                _bookList.value!!.forEach {
                    if(dataManager.getBookInfo(it.id) != 0){
                        readingBooks.add(it)
                    }
                }
                _bookList.value!!.forEach {
                    if (dataManager.getBookInfo(it.id) >= it.count_page-5){
                        finishedBooks.add(it)
                    }
                }
                idle()
            }
            else{
                error(bookResponse.message())
            }
        }
    }

    fun getBookInfo(bookId:Int):Int{
        return dataManager.getBookInfo(bookId)
    }
}