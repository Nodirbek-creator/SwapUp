package com.example.handybook.data.repository

import com.example.handybook.data.model.Login
import com.example.handybook.data.model.SignUp
import com.example.handybook.data.model.User
import com.example.handybook.data.network.ApiService
import retrofit2.Response

class AuthRepository(private val apiService: ApiService) {

    suspend fun login(login: Login): Response<User>{
        return apiService.login(login)
    }
    suspend fun signUp(signUp: SignUp): Response<User>{
        return apiService.signUp(signUp)
    }
}