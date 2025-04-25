package com.example.handybook.db

import android.content.SharedPreferences
import com.example.handybook.dataclass.User

class DataManager(private val sharedPreferences: SharedPreferences) {
    fun saveUser(user: User){
        val editor = sharedPreferences.edit()
        editor.putInt("id", user.id)
        editor.putString("username", user.username)
        editor.putString("fullname", user.fullname)
        editor.putString("token", user.access_token)
    }

    fun getUser():User{
        val id = sharedPreferences.getInt("id",0)
        val username = sharedPreferences.getString("username", "") ?: "ali23"
        val fullname = sharedPreferences.getString("fullname", "ali") ?: "ali"
        val token = sharedPreferences.getString("token", "34tga5") ?: "234gt112"
        return User(id, username, fullname, token)
    }
}