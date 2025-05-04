package com.example.handybook.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val errors: Errors
)