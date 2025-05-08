package com.example.swapup.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapup.data.model.Book
import com.example.swapup.data.model.Comment
import com.example.swapup.data.network.RetrofitInstance
import com.example.swapup.data.repository.BookRepository
import com.example.swapup.data.repository.FirestoreRepo
import com.example.swapup.data.sharedpref.DataManager
import com.example.swapup.viewmodel.state.UiState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FireStoreViewModel(
    private val dataManager: DataManager,
    bookViewModel: BookViewModel
): ViewModel() {
    val bookRepository = BookRepository(RetrofitInstance.api)
    val firebaseRepository = FirestoreRepo(FirebaseFirestore.getInstance())
    val currentUser = dataManager.getUser()

    private val _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState: State<UiState> get() = _uiState

    private val bookId = bookViewModel.selectedBook

    private val _selectedBook = MutableLiveData(Book())

    private val _commentList = MutableLiveData<List<Comment>>()
    val commentList: LiveData<List<Comment>> get() = _commentList

    var bookType by mutableStateOf("E-Book")
        private set

    fun changeBookType(newType: String){
        bookType = newType
    }

    var bookInfo by mutableStateOf("Tavsif")
        private set

    fun changeBookInfo(newInfo: String){
        bookInfo = newInfo
    }
    private val _isBookSaved = MutableLiveData<Boolean>()
    val isBookSaved get() = _isBookSaved

    init {
        viewModelScope.launch {
            val stage1 = async { loadInitialData() }
            stage1.await()
            delay(200)
            isSaved()
        }

    }

    fun isSaved(){
        viewModelScope.launch {
            try {
                val response = async { firebaseRepository.isBookSaved(currentUser, _selectedBook.value?: Book()) }
                val result = response.await()
                if(result){
                    _isBookSaved.value = true
                }
                else{
                    _isBookSaved.value = false
                }
            } catch (e: Exception){
                error("${e.localizedMessage}")
            }
        }
    }
    fun loadInitialData(){
        viewModelScope.launch {
            try {
                loading()
                val book = async { bookRepository.getBookById(bookId) }
                val comment = async { bookRepository.getCommentById(bookId) }
                val bookResponse = book.await()
                val commentResponse = comment.await()
                if(bookResponse.isSuccessful && commentResponse.isSuccessful){
                    _commentList.value = commentResponse.body()
                    _selectedBook.value = bookResponse.body()
                    idle()
                }
                else{
                    error("Failed to load data")
                }
            } catch (e: Exception){
                error("${e.localizedMessage}")
            }
        }
    }

    fun saveBook(){
        viewModelScope.launch {
            try {
                loading()
                val result = firebaseRepository.saveBook(_selectedBook.value!!, currentUser)
                if(result){
                    _isBookSaved.value = true
                    idle()
                }
                else{
                    error("Couldn't save the book")
                }
            } catch (e: Exception){
                error("${e.localizedMessage}")
            }
        }
    }
    fun unsaveBook(){
        viewModelScope.launch {
            try {
                val result = firebaseRepository.unsaveBook(_selectedBook.value!!, currentUser)
                if(result){
                    isBookSaved.value = false
                    idle()
                }
                else{
                    error("Couldn't unsave the book")
                }
            } catch (e: Exception){
                error("${e.localizedMessage}")
            }
        }
    }


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