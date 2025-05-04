package com.example.swapup.connection

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe(): Flow<Status>

    enum class Status{
        Available,Unavailable,Lost,Losing
    }
}