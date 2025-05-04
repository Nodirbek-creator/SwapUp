package com.example.swapup.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorBody(
    val username: List<String>,
    val password: List<String>,
)
