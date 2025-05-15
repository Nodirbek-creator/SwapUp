package com.example.swapup.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapup.data.model.Login
import com.example.swapup.data.repository.AuthRepository
import com.example.swapup.data.repository.FirebaseAuthRepo
import com.example.swapup.data.result.AuthResult
import com.example.swapup.viewmodel.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: FirebaseAuthRepo,
): ViewModel() {
//    private val repository = AuthRepository(RetrofitInstance.api)


    private val _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState: State<UiState> get() = _uiState

    var username by mutableStateOf("")
        private set

    var usernameError by mutableStateOf(false)
        private set

    var password by mutableStateOf("")
        private set

    var passwordError by mutableStateOf(false)
        private set

    var passwordVisible by mutableStateOf(false)
        private set

    fun onUsernameChange(newUsername: String){
        username = newUsername
    }
    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun usernameValid(username: String){
        val regex = Regex("^@[a-zA-Z0-9_]{5,}$")
        usernameError = !regex.matches(username)
    }

    fun passwordValid(password: String){
        passwordError = password.length < 8
    }

    fun toggleVisibility(){
        passwordVisible = !passwordVisible
    }
    fun resetUiState(){
        _uiState.value = UiState.Idle
    }

    fun login(){
        viewModelScope.launch {
            usernameValid(username)
            passwordValid(password)
            if(!passwordError && !usernameError){
                _uiState.value = UiState.Loading
                try {
                    val response = repository.login(Login(username,password))
                    when(response){
                        is AuthResult.Success ->{
                            _uiState.value = UiState.Success
                            Log.d(TAG,"Login Success")
                            delay(1000)
                            _uiState.value = UiState.Idle
                        }
                        is AuthResult.Error ->{
                            _uiState.value = UiState.Error(
                                response.msg
                            )
                        }
                    }
                } catch (e: Exception){
                    _uiState.value = UiState.Error("Login failed")
                    Log.e(TAG,"Login Failed", e)
                }
            }
        }
    }
    companion object{
        private val TAG = "LoginVM"
    }
}