package com.example.swapup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapup.data.network.RetrofitInstance
import com.example.swapup.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import retrofit2.http.Query

class SearchViewModel(
    initialQuery: String
): ViewModel() {

    private val repository = BookRepository(RetrofitInstance.api)

    private val _searchQuery = MutableStateFlow(initialQuery)
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()


    val bookList = _searchQuery
        .debounce(300)
        .flatMapLatest { query->
            repository.searchBook(query)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun updateQuery(newQuery: String){
        _searchQuery.value = newQuery
    }


}