package com.example.handybook.data.result

import com.example.handybook.data.model.User

sealed class AuthResult {
    data class Success(val user: User): AuthResult()
    data class Error(val msg: String): AuthResult()
}