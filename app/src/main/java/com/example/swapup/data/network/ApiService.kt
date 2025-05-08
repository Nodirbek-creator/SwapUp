package com.example.swapup.data.network

import com.example.swapup.data.model.Book
import com.example.swapup.data.model.Category
import com.example.swapup.data.model.Comment
import com.example.swapup.data.model.Login
import com.example.swapup.data.model.SignUp
import com.example.swapup.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    //http://handybook.uz/book-api
    @GET("/book-api")
    suspend fun getAllBooks(): Response<List<Book>>

    //http://handybook.uz/book-api/view?id=1
    @GET("/book-api/view")
    suspend fun getBookById(@Query("id") id:Int): Response<Book>

    //http://handybook.uz/book-api/main-book
    @GET("/book-api/main-book")
    suspend fun getMainBook(): Response<Book>

    //http://handybook.uz/book-api/all-category
    @GET("/book-api/all-category")
    suspend fun getAllCategories(): Response<List<Category>>

    //http://handybook.uz/book-api/category?name=Badiiy adabiyot
    @GET("/book-api/category")
    suspend fun getBooksByCategory(@Query("name") name: String): Response<List<Book>>

    //http://handybook.uz/book-api/search-name?name=yulduz
    @GET("/book-api/search-name")
    suspend fun searchBook(@Query("name") name: String): Response<List<Book>>

    //http://handybook.uz/book-api/login
    @POST("/book-api/login")
    suspend fun login(@Body login: Login): Response<User>

    //http://handybook.uz/book-api/register
    @POST("/book-api/register")
    suspend fun signUp(@Body signUp: SignUp): Response<User>

    @GET("book-api/comment")
    suspend fun getCommentsById(@Query("id") id:Int): Response<List<Comment>>
}