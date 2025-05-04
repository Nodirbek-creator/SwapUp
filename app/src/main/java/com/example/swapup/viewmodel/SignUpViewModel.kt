package com.example.swapup.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapup.data.model.SignUp
import com.example.swapup.data.repository.AuthRepository
import com.example.swapup.data.result.AuthResult
import com.example.swapup.viewmodel.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val repository: AuthRepository) :ViewModel(){

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

    var fullname by mutableStateOf("")
        private set

    var fullnameError by mutableStateOf(false)
        private set

    var email by mutableStateOf("")
        private set

    var emailError by mutableStateOf(false)
        private set
    var passwordVisible by mutableStateOf(false)
        private set

    fun onUsernameChange(newUsername: String){
        username = newUsername
    }
    fun usernameValid(username: String){
        val regex = Regex("^@[a-zA-Z0-9_]{5,}$")
        usernameError = !regex.matches(username)
    }

    fun onFullnameChange(newFullname: String){
        fullname = newFullname
    }
    fun fullnameValid(fullname: String){
        val regex = Regex("^[A-Z][a-zA-Z'-]+(\\s[A-Z][a-zA-Z'-]+)+$")
        fullnameError = !regex.matches(fullname.trim())
    }

    fun resetUiState(){
        _uiState.value = UiState.Idle
    }
    fun onEmailChange(newEmail: String) {
        email = newEmail
    }
    fun emailValid(email: String){
        emailError = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }
    fun passwordValid(password: String){
        passwordError = password.length < 8
    }

    fun toggleVisibility(){
        passwordVisible = !passwordVisible
    }

    fun signUp(){
        viewModelScope.launch {
            usernameValid(username)
            passwordValid(password)
            emailValid(email)
            fullnameValid(fullname)
            if(!usernameError && !passwordError && !fullnameError && !emailError){
                _uiState.value = UiState.Loading
                try {
                    val response = repository.signUp(SignUp(username, fullname, email, password))
                    when(response){
                        is AuthResult.Success->{
                            _uiState.value = UiState.Success
                            Log.d("Login","Success")
                            delay(1000)
                            _uiState.value = UiState.Idle
                            onUsernameChange("")
                            onPasswordChange("")
                            onFullnameChange("")
                            onEmailChange("")
                        }
                        is AuthResult.Error->{
                            _uiState.value = UiState.Error(
                                response.msg
                            )
                        }
                    }
                } catch (e: Exception){
                    _uiState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
                }
            }
        }
    }

}