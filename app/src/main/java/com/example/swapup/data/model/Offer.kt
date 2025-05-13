package com.example.swapup.data.model

data class Offer(
    val uid: String = "",
    val title: String = "",
    val author: String = "",
    val language: String = Language.Unspecified.name,
    val photo: String = "",
    val description: String = "",
    val active: Boolean = true,
    val owner: String = "",
)
