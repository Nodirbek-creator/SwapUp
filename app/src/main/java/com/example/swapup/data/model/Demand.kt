package com.example.swapup.data.model

data class Demand(
    val uid: String = "",
    val title: String = "",
    val author: String = "",
    val language: String = Language.Unspecified.name,
    val photo: String = "",
    val description: String = "",
    val active: Boolean = true,
    val owner: String = "",
    val latitude: Double = 0.00,
    val longitude: Double = 0.00
)
