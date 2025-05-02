package com.example.handybook.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handybook.data.model.Login
import com.example.handybook.data.model.User
import com.example.handybook.data.network.RetrofitInstance
import com.example.handybook.data.repository.AuthRepository
import com.example.handybook.data.result.AuthResult
import com.example.handybook.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository,
): ViewModel() {
//    private val repository = AuthRepository(RetrofitInstance.api)


    private val _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState: State<UiState> get() = _uiState

    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var passwordVisible by mutableStateOf(false)
        private set

    fun onUsernameChange(newUsername: String){
        username = newUsername
    }
    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun toggleVisibility(){
        passwordVisible = !passwordVisible
    }
    fun resetUiState(){
        _uiState.value = UiState.Idle
    }

    fun login(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = repository.login(Login(username,password))
                when(response){
                    is AuthResult.Success ->{
                        _uiState.value = UiState.Success
                        Log.d("Login","Success")
                        delay(1000)
                        _uiState.value = UiState.Idle
                        onUsernameChange("")
                        onPasswordChange("")
                    }
                    is AuthResult.Error ->{
                        _uiState.value = UiState.Error(
                            response.msg
                        )
                    }
                }
            } catch (e: Exception){
                _uiState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
                Log.d("Login","${e.localizedMessage}")
            }
        }
    }
}