package com.example.swapup.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapup.data.network.RetrofitInstance
import com.example.swapup.data.repository.BookRepository
import com.example.swapup.data.repository.SearchRepository
import com.example.swapup.data.sharedpref.DataManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(
    initialQuery: String,
    dataManager: DataManager
): ViewModel() {

    private val repository = BookRepository(RetrofitInstance.api)
    private val rep = SearchRepository(dataManager)

    private val _searchQuery = MutableStateFlow(initialQuery)
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val bookList = _searchQuery
        .debounce(10)
        .flatMapLatest { query->
            repository.searchBook(query)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private var _searchHistory = mutableStateOf("")
    val searchHistory: State<String> = _searchHistory

    fun updateQuery(newQuery: String){
        _searchQuery.value = newQuery
    }

    fun getHistory(){
        viewModelScope.launch {
            _searchHistory.value = rep.getSearchHistory()
        }
    }

    fun writeHistory(bookId:Int){
        viewModelScope.launch {
            if(searchHistory.value.isEmpty()){
                rep.writeSearchHistory(bookId)
            }else if (checkForRepetition(bookId)){
                rep.writeSearchHistory(bookId)
            }else{
                rep.removeFromHistory(bookId)
                rep.writeSearchHistory(bookId)
            }
            getHistory()
        }
    }

    private fun checkForRepetition(bookId:Int):Boolean{
        searchHistory.value.split(":").forEach {
            if(it != ""){
                if (it.toInt() == bookId) {
                    return false
                }
            }
        }
        return true
    }

    fun clearHistory(){
        viewModelScope.launch {
            rep.clearHistory()
            getHistory()
        }
    }

}