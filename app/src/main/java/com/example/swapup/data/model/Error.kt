package com.example.swapup.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val errors: Errors
)