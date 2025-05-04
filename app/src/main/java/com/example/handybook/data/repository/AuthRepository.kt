package com.example.handybook.data.repository

import android.util.Log
import com.example.handybook.data.model.Error
import com.example.handybook.data.model.Login
import com.example.handybook.data.model.SignUp
import com.example.handybook.data.network.ApiService
import com.example.handybook.data.result.AuthResult
import com.example.handybook.data.sharedpref.DataManager
import kotlinx.serialization.json.Json
import okio.IOException

class AuthRepository(
    private val apiService: ApiService,
    private val dataManager: DataManager) {

    suspend fun login(login: Login): AuthResult{
        return try {
            val response = apiService.login(login)

            if(response.isSuccessful){
                val user = response.body()
                if(user != null && user.access_token.isNotEmpty()){
                    dataManager.saveUser(user)
                    Log.d("usernull","${dataManager.getUser()}")
                    AuthResult.Success(user)
                }
                else{
                    Log.d("usernull","${dataManager.getUser()}")
                    AuthResult.Error("Unknown error")
                }
            }
            else{
                when(response.code()){
                    422-> {
                        AuthResult.Error("Login failed. Invalid credentials.")
                    }
                    else-> {
                        AuthResult.Error("Unknown error")
                    }
                }
            }
        } catch (e: IOException){
            AuthResult.Error("Network error: ${e.localizedMessage}")
        } catch (e: Exception){
            AuthResult.Error("Unknown error: ${e.localizedMessage}")
        }
    }
    suspend fun signUp(signUp: SignUp): AuthResult{
        return try {
            val response = apiService.signUp(signUp)

            if(response.isSuccessful){
                val user = response.body()
                if(user != null && user.access_token.isNotEmpty()){
                    dataManager.saveUser(user)
                    AuthResult.Success(user)
                }
                else{
                    AuthResult.Error("Unknown error")
                }
            }
            else{
                when(response.code()){
                    500-> {
                        AuthResult.Error("User with such email already exists")
                    }
                    422-> {
                        val errorString = response.errorBody()?.string() ?: ""
                        val error = Json.decodeFromString<Error>(errorString)
                        val userName = error.errors.username
                        val password = error.errors.password
                        val space = if(userName ==null) "" else "\n"
                        AuthResult.Error("${userName?.get(0) ?: ""}$space${password?.get(0)?: ""}")
                    }
                    else-> {
                        AuthResult.Error("Unknown error")
                    }
                }
            }

        } catch (e: IOException){
            AuthResult.Error("Network error: ${e.localizedMessage}")
        } catch (e: Exception){
            Log.d("Json", "${e.message}")
            AuthResult.Error("Unknown error: ${e.localizedMessage}")
        }
    }
    suspend fun logout(){
        dataManager.removeUser()
    }

}