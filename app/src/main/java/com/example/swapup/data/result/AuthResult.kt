package com.example.swapup.data.result

import com.example.swapup.data.model.User

sealed class AuthResult {
    data class Success(val user: User): AuthResult()
    data class Error(val msg: String): AuthResult()
}