package com.example.swapup.data.repository

import android.content.Context
import android.util.Log
import com.example.swapup.R
import com.example.swapup.data.model.Login
import com.example.swapup.data.model.SignUp
import com.example.swapup.data.model.User
import com.example.swapup.data.result.AuthResult
import com.example.swapup.data.sharedpref.DataManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepo(
    private val fs: FirebaseFirestore,
    private val dataManager: DataManager,
    private val context: Context,) {
    val userRef = fs.collection("users")
    suspend fun signUp(signUp: SignUp): AuthResult{
        try {
            val username = signUp.username.lowercase()
            val email = signUp.email.lowercase()
            // 1. Check if username is already taken
            val usernameQuery: QuerySnapshot = userRef
                .whereEqualTo("username", username)
                .get()
                .await()

            if (!usernameQuery.isEmpty) {
                return AuthResult.Error(context.getString(R.string.username_exists))
            }

            // 2. Check if email is already taken
            val emailQuery: QuerySnapshot = userRef
                .whereEqualTo("email", email)
                .get()
                .await()

            if (!emailQuery.isEmpty) {
                return AuthResult.Error(context.getString(R.string.email_exists))
            }

            //randomly generated token
//            val token = UUID.randomUUID().toString()
            val user = User(
                username = signUp.username.lowercase(),
                fullname = signUp.fullname,
                email = signUp.email.lowercase(),
                password = signUp.password
            )
            val doc = userRef.document()
            val userWithId = user.copy(id = doc.id)
            doc.set(userWithId).await()
            dataManager.saveUser(userWithId)
            return AuthResult.Success(userWithId)
        } catch (e: Exception){
            Log.e(TAG,"Sign up failed",e)
            return AuthResult.Error(context.getString(R.string.signup_failed))
        }
    }

    suspend fun login(login: Login): AuthResult{
        try {
            val result = userRef
                .whereEqualTo("username", login.username.lowercase())
                .get()
                .await()
            if(result.isEmpty){
                return AuthResult.Error(context.getString(R.string.username_notFound))
            }
            val user = result.documents.first()
            val storedPassword = user.getString("password")

            if(storedPassword == login.password){
                val loggedUser = user.toObject<User>() ?: User()
                dataManager.saveUser(loggedUser)
                return AuthResult.Success(loggedUser)
            }
            return AuthResult.Error(context.getString(R.string.wrong_password))
        } catch (e: Exception){
            Log.e(TAG,"Login failed",e)
            return AuthResult.Error(context.getString(R.string.login_failed))
        }
    }
    suspend fun logout(){
        try {
            val user = dataManager.getUser()
            val result = userRef
                .document(user.id)
                .delete()
                .await()
            dataManager.removeUser()
        } catch (e: Exception){
            Log.e(TAG,"Logout failed",e)
        }
    }
    companion object{
        private val TAG = "FirebaseAuthRepo"
    }
}