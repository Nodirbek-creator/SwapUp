package com.example.handybook.network

import com.example.handybook.dataclass.Book
import com.example.handybook.dataclass.Category
import com.example.handybook.dataclass.Login
import com.example.handybook.dataclass.SignUp
import com.example.handybook.dataclass.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    //http://handybook.uz/book-api
    @GET("/book-api")
    fun getAllBooks(): Call<List<Book>>

    //http://handybook.uz/book-api/view?id=1
    @GET("/book-api/view")
    fun getBookById(@Query("id") id:Int): Call<Book>

    //http://handybook.uz/book-api/main-book
    @GET("/book-api/main-book")
    fun getMainBook(): Call<Book>

    //http://handybook.uz/book-api/all-category
    @GET("/book-api/all-category")
    fun getAllCategories(): Call<List<Category>>

    //http://handybook.uz/book-api/category?name=Badiiy adabiyot
    @GET("/book-api/category")
    fun getBooksByCategory(@Query("name") name: String): Call<List<Book>>

    //http://handybook.uz/book-api/search-name?name=yulduz
    @GET("/book-api/search-name")
    fun searchBook(@Query("name") name: String): Call<Book>

    //http://handybook.uz/book-api/login
    @POST("/book-api/login")
    fun login(@Body login: Login): Call<User>

    //http://handybook.uz/book-api/register
    @POST("/book-api/register")
    fun signUp(@Body signUp: SignUp): Call<User>

}