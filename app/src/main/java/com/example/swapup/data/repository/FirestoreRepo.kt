package com.example.swapup.data.repository

import android.util.Log
import com.example.swapup.data.model.Book
import com.example.swapup.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class FirestoreRepo(private val firestore: FirebaseFirestore ) {

    private val savedRef = firestore.collection("saved")
    suspend fun getSavedBooks(user: User): List<Book>{
        return try {
            val snapshot = savedRef
                .document(user.username)
                .collection("savedBooks")
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject<Book>() }
        } catch (e: Exception){
            emptyList()
        }
    }
    suspend fun isBookSaved(user: User, book: Book): Boolean{
        return try {
            val snapshot = savedRef
                .document(user.username)
                .collection("savedBooks")
                .get()
                .await()
            Log.d("book","result: ${book}")
            val book = snapshot.documents.mapNotNull { it.toObject<Book>() }.find{ it.equals(book)}
            if(book == null){
                false
            }
            else{
                true
            }
        } catch (e: Exception){
            Log.w(TAG, "Error saving book", e)
            false
        }
    }
    suspend fun saveBook(book: Book, user: User): Boolean{
        return try {
            val reference = savedRef
                .document(user.username)
                .collection("savedBooks")
                .document(book.name)
                .set(book)
                .await()
            Log.d(TAG,"Book is saved!")
            true
        }
        catch (e: Exception){
            Log.w(TAG, "Error saving book", e)
            false
        }
    }
    suspend fun unsaveBook(book: Book, user: User): Boolean{
        return try {
            val refer = firestore
                .collection("saved")
                .document(user.username)
                .collection("savedBooks")
                .document(book.name)
                .delete()
                .await()
            Log.d(TAG,"Book is saved!")
            true
        } catch (e: Exception){
            Log.w(TAG, "Error saving book", e)
            false
        }
    }

    companion object {
        private const val TAG = "BookRepository"
    }
}