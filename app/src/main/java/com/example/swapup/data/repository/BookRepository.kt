package com.example.swapup.data.repository

import com.example.swapup.data.model.Book
import com.example.swapup.data.model.Category
import com.example.swapup.data.model.Comment
import com.example.swapup.data.network.ApiService
import com.example.swapup.data.sharedpref.DataManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class BookRepository(
    private val apiService: ApiService
){

    suspend fun getCategories(): Response<List<Category>>{
        return apiService.getAllCategories()
    }

    suspend fun getAllBooks(): Response<List<Book>>{
        return apiService.getAllBooks()
    }

    suspend fun getBooksByCategory(category: String): Response<List<Book>>{
        return apiService.getBooksByCategory(category)
    }

    suspend fun getMainBook(): Response<Book>{
        return apiService.getMainBook()
    }

    suspend fun getBookById(id: Int): Response<Book>{
        return apiService.getBookById(id)
    }

    fun searchBook(query: String): Flow<List<Book>> = flow {
        val response = apiService.searchBook(query)
        if (response.isSuccessful){
            emit(response.body() ?: emptyList())
        }
    }

    suspend fun getCommentById(id: Int): Response<List<Comment>> {
        return apiService.getCommentsById(id)
    }
}