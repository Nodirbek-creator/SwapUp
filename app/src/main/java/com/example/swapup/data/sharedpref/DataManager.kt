package com.example.swapup.data.sharedpref

import android.content.Context
import androidx.activity.ComponentActivity
import com.example.swapup.data.model.User

class DataManager(private val activity: ComponentActivity) {
    private val sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
    fun saveUser(user: User){
        val editor = sharedPreferences.edit()
        editor.putInt("id", user.id)
        editor.putString("username", user.username)
        editor.putString("fullname", user.fullname)
        editor.putString("token", user.access_token)
        editor.apply()
    }

    fun removeUser(){
        val editor = sharedPreferences.edit()
        editor.putInt("id", -1)
        editor.putString("username", "")
        editor.putString("fullname", "")
        editor.putString("token", "")
        editor.apply()
    }
    fun getUser(): User {
        val id = sharedPreferences.getInt("id",-1)
        val username = sharedPreferences.getString("username", "abcd@gmail.com") ?: "null"
        val fullname = sharedPreferences.getString("fullname", "Azizov Ali") ?: "null"
        val token = sharedPreferences.getString("token", "") ?: ""
        return User(id, username, fullname, token)
    }

    fun userLogged(): Boolean{
        return getUser().id != -1 && getUser().access_token.isNotEmpty()
    }
}