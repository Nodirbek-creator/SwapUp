package com.example.swapup.data.repository

import com.example.swapup.data.sharedpref.DataManager

class SearchRepository(
    private val dataManager: DataManager
) {
    suspend fun getSearchHistory(): String{
        return dataManager.getSearchHistory()
    }

    suspend fun writeSearchHistory(bookId: Int){
        dataManager.saveSearchHistory(bookId)
    }

    suspend fun removeFromHistory(bookId: Int){
        dataManager.removedFromHistory(bookId)
    }
}