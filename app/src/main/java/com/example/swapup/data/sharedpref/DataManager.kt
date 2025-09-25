package com.example.swapup.data.sharedpref

import android.content.Context
import androidx.activity.ComponentActivity
import com.example.swapup.data.model.User

class DataManager(private val activity: ComponentActivity) {
    private val sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
    fun clearDB(){
        sharedPreferences.edit().clear().apply()
    }
    fun saveUser(user: User){
        val editor = sharedPreferences.edit()
        editor.putString("id", user.id)
        editor.putString("username", user.username)
        editor.putString("fullname", user.fullname)
        editor.putString("email", user.email)
        editor.putString("password", user.password)
        editor.apply()
    }

    fun removeUser(){
        val editor = sharedPreferences.edit()
        editor.putString("id", "")
        editor.putString("username", "")
        editor.putString("fullname", "")
        editor.putString("email", "")
        editor.putString("password", "")
        editor.apply()
    }
    fun getUser(): User {
        val id = sharedPreferences.getString("id","") ?: ""
        val username = sharedPreferences.getString("username", "") ?: ""
        val fullname = sharedPreferences.getString("fullname", "") ?: "null"
        val email = sharedPreferences.getString("email", "") ?: "null"
        val token = sharedPreferences.getString("password", "") ?: ""
        return User(id, username, fullname, email, token)
    }

    fun userLogged(): Boolean{
        return getUser().id != "" && getUser().username.isNotEmpty()
    }

   fun getSearchHistory(): String{
       return sharedPreferences.getString("bookId", "") ?: ""
   }

    fun saveSearchHistory(bookId:Int){
        val editor = sharedPreferences.edit()
        if(getSearchHistory().split(":").size<9){
            editor.putString("bookId", "${getSearchHistory()}$bookId:")
        }else{
            editor.putString("bookId", "${getSearchHistory().substring(1)}$bookId:")
        }
        editor.apply()
    }

    fun removedFromHistory(bookId: Int){
        val editor = sharedPreferences.edit()
        var newString = ""
        getSearchHistory().split(":").forEach {
            if(
                it != ""
            ){
                if (it.toInt() != bookId) {
                    newString = "${newString}$it:"
                }
            }
        }
        editor.putString("bookId", newString)
        editor.apply()
    }

    fun clearHistory(){
        val editor = sharedPreferences.edit()
        editor.putString("bookId", "")
        editor.apply()
    }

    fun saveBookInfo(bookId:Int, page: Int){
        val editor = sharedPreferences.edit()
        editor.putInt("$bookId", page)
        editor.apply()
    }

    fun getBookInfo(bookId: Int): Int{
        return sharedPreferences.getInt("$bookId", 0)
    }
}