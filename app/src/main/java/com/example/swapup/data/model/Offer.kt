package com.example.swapup.data.model

data class Offer(
    val uid: String = "",
    val title: String = "",
    val author: String = "",
    val language: Language = Language.Unspecified,
    val photo: String = "",
    val description: String = "",
    val active: Boolean = true,
    val publisher: String = "",
)
