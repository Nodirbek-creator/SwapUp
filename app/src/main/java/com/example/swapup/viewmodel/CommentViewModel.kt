package com.example.swapup.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapup.data.model.Comment
import com.example.swapup.data.network.RetrofitInstance
import com.example.swapup.data.repository.BookRepository
import com.example.swapup.viewmodel.state.UiState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CommentViewModel: ViewModel() {
    private val repository = BookRepository(RetrofitInstance.api)

    private val _uiState = mutableStateOf<UiState>(UiState.Loading)
    val uiState: State<UiState> get() = _uiState

    val comments = mutableStateListOf<Comment>()

    init {
        loadInitialData()
    }

    private fun loadInitialData(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val a = 10

            for(i in 0..a){
                val tempComments = async { repository.getCommentById(i) }

                val response = tempComments.await()

                if(response.isSuccessful && !response.body().isNullOrEmpty()){
                    comments.addAll(response.body()!!)
                }
            }

            _uiState.value = UiState.Success
        }
    }
}