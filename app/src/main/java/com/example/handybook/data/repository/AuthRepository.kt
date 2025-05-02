package com.example.handybook.data.repository

import com.example.handybook.data.model.Login
import com.example.handybook.data.model.SignUp
import com.example.handybook.data.model.User
import com.example.handybook.data.network.ApiService
import com.example.handybook.data.result.AuthResult
import com.example.handybook.data.sharedpref.DataManager
import okio.IOException
import retrofit2.Response

class AuthRepository(
    private val apiService: ApiService,
    private val dataManager: DataManager) {


    suspend fun login(login: Login): AuthResult{
        return try {
            val response = apiService.login(login)

            if(response.isSuccessful){
                val user = response.body()
                if(user != null && user.access_token.isNotEmpty()){
                    dataManager.loginUser(user)
                    AuthResult.Success(
                        user
                    )

                }
                else{
                    AuthResult.Error(
                        "Login failed. Invalid credentials."
                    )
                }
            } else{
                AuthResult.Error(
                    "Server error: ${response.code()}"
                )
            }
        } catch (e: IOException){
            AuthResult.Error("Network error: ${e.localizedMessage}")
        } catch (e: Exception){
            AuthResult.Error("Unknown error: ${e.localizedMessage}")
        }
    }
    suspend fun signUp(signUp: SignUp): Response<User>{
        return apiService.signUp(signUp)
    }
}