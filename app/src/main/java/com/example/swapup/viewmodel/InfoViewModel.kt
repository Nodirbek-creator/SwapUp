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
import kotlinx.coroutines.launch

class InfoViewModel(
    private val dataManager: DataManager
): ViewModel() {
    val bookRepository = BookRepository(RetrofitInstance.api)
    val firebaseRepository = FirestoreRepo(FirebaseFirestore.getInstance())
    val currentUser = dataManager.getUser()

    private val _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState: State<UiState> get() = _uiState

    var selectedBook by mutableStateOf<Book?>(null)
        private set

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

    fun fetchBookInfo(bookId:Int){
        viewModelScope.launch {
            loading()
            val response = bookRepository.getBookById(bookId)
            val response2 = bookRepository.getCommentById(bookId)
            if(response.isSuccessful && response2.isSuccessful){
                selectedBook = response.body()
                _commentList.value = response2.body()
                try {
                    val response3 = async { firebaseRepository.isBookSaved(currentUser, selectedBook?: Book()) }
                    val result = response3.await()
                    if(result){
                        _isBookSaved.value = true
                    }
                    else{
                        _isBookSaved.value = false
                    }
                } catch (e: Exception){
                    error("THIS IS THE MESSAGE${e.localizedMessage}")
                }
                success()
            }else{
                _uiState.value = UiState.Error(response.message()+"\n\b"+response2.message())
            }
        }
    }

    fun clearCache(){
        selectedBook = null
        bookInfo = "Tavsif"
        bookType = "E-Book"
    }

    fun getBookInfo(bookId: Int): Int {
        return dataManager.getBookInfo(bookId)
    }

    fun saveBook(){
        viewModelScope.launch {
            try {
                loading()
                val result = firebaseRepository.saveBook(selectedBook!!, currentUser)
                if(result){
                    _isBookSaved.value = true
                    success()
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
                val result = firebaseRepository.unsaveBook(selectedBook!!, currentUser)
                if(result){
                    isBookSaved.value = false
                    success()
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