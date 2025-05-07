package com.example.swapup.data.repository

import com.example.swapup.data.model.Book
import com.example.swapup.data.model.Category
import com.example.swapup.data.model.Comment
import com.example.swapup.data.network.ApiService
import retrofit2.Response

class BookRepository(private val apiService: ApiService){

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

    suspend fun searchBook(query: String): Response<Book>{
        return apiService.searchBook(query)
    }

    suspend fun getCommentById(id: Int): Response<List<Comment>> {
        return apiService.getCommentsById(id)
    }
}