package com.example.handybook.data.sharedpref

import android.content.SharedPreferences
import com.example.handybook.data.model.User

class DataManager(private val sharedPreferences: SharedPreferences) {
    fun saveUser(user: User){
        val editor = sharedPreferences.edit()
        editor.putInt("id", user.id)
        editor.putString("username", user.username)
        editor.putString("fullname", user.fullname)
        editor.putString("token", user.access_token)
    }

    fun getUser(): User {
        val id = sharedPreferences.getInt("id",0)
        val username = sharedPreferences.getString("username", "abcd@gmail.com") ?: "abcd@gmail.com"
        val fullname = sharedPreferences.getString("fullname", "Azizov Ali") ?: "Azizov Ali"
        val token = sharedPreferences.getString("token", "34tga5") ?: "234gt112"
        return User(id, username, fullname, token)
    }
}