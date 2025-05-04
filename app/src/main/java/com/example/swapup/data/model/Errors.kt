package com.example.swapup.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Errors(
    val password: List<String>? = null,
    val username: List<String>? = null
)