package com.example.swapup.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapup.data.model.Book
import com.example.swapup.data.model.Category
import com.example.swapup.data.network.RetrofitInstance
import com.example.swapup.data.repository.BookRepository
import com.example.swapup.viewmodel.state.UiState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class BookViewModel: ViewModel() {
    val repository = BookRepository(RetrofitInstance.api)

    private val _uiState = mutableStateOf<UiState>(UiState.Loading)
    val uiState: State<UiState> get() = _uiState

    var searchQuery by mutableStateOf("")
        private set

    var selectedCategory by mutableStateOf(Category("Barchasi"))
        private set

    private val _mainBook = MutableLiveData<Book>()
    val mainBook: LiveData<Book> get() = _mainBook

    private val _categoryList = MutableLiveData<List<Category>>()
    val categoryList: LiveData<List<Category>> get() = _categoryList

    private val _bookList = MutableLiveData<List<Book>>()
    val bookList: LiveData<List<Book>> get() = _bookList



    init {
        loadInitialData()
    }

    fun getBookById(id: Int){
        viewModelScope
    }

    fun onCategoryChange(newCategory: Category){
        selectedCategory = newCategory
        viewModelScope.launch {
            fetchBooks(selectedCategory)
        }
    }
    fun onQueryChange(newQuery: String){
        searchQuery = newQuery
    }

    private fun loadInitialData(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val categories = async { repository.getCategories() }
            val mainBook = async { repository.getMainBook() }
            val allBooks = async { repository.getAllBooks() }

            val response1 = categories.await()
            val response2 = mainBook.await()
            val response3 = allBooks.await()

            if(response1.isSuccessful && response2.isSuccessful && response3.isSuccessful){
                val fullList = listOf(Category("Barchasi")) + (response1.body() ?: emptyList())
                _categoryList.value = fullList
                _mainBook.value = response2.body()
                _bookList.value = response3.body()
                _uiState.value = UiState.Success
            }
            else{
                _uiState.value = UiState.Error("Failed to load data")
            }
        }
    }



    fun fetchBooks(category: Category){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            if(category.typename == "Barchasi"){
                val response = repository.getAllBooks()
                if(response.isSuccessful){
                    _uiState.value = UiState.Success
                    _bookList.value = response.body()
                }
            }
            else{
                val response = repository.getBooksByCategory(category.typename)
                if(response.isSuccessful){
                    _uiState.value = UiState.Success
                    _bookList.value = response.body()
                }
            }
        }
    }



}