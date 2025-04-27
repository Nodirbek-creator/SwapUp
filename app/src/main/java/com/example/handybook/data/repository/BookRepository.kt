package com.example.handybook.data.repository

import com.example.handybook.data.model.Book
import com.example.handybook.data.model.Category
import com.example.handybook.data.network.ApiService
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
}