package com.example.handybook.viewmodel

import android.content.Context
import android.util.Log
import android.util.Patterns.EMAIL_ADDRESS
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handybook.data.model.Login
import com.example.handybook.data.model.SignUp
import com.example.handybook.data.model.User
import com.example.handybook.data.network.RetrofitInstance
import com.example.handybook.data.repository.AuthRepository
import com.example.handybook.data.sharedpref.DataManager
import com.example.handybook.state.UiState
import kotlinx.coroutines.launch

class AuthViewModel(): ViewModel() {
    private val repository = AuthRepository(RetrofitInstance.api)

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    private val _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState: State<UiState> get() = _uiState

    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var fullname by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var passwordVisible by mutableStateOf(false)
        private set

    fun onUsernameChange(newUsername: String){
        username = newUsername
    }

    fun onFullnameChange(newFullname: String){
        fullname = newFullname
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }


    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun toggleVisibility(){
        passwordVisible = !passwordVisible
    }


    fun login(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = repository.login(Login(username,password))
                if(response.isSuccessful){
                    _uiState.value = UiState.Success
                    _currentUser.value = response.body()
                    Log.d("Login","Success")
                }
            } catch (e: Exception){
                _uiState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
                Log.d("Login","${e.localizedMessage}")
            }
        }
    }

    fun signUp(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = repository.signUp(SignUp(username, fullname, email, password))
                if(response.isSuccessful){
                    _uiState.value = UiState.Success
                    _currentUser.value = response.body()
                }
            } catch (e: Exception){
                _uiState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun logOut(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading

        }
    }
}