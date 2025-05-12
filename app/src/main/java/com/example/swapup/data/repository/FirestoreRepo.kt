package com.example.swapup.data.repository

import android.util.Log
import com.example.swapup.data.model.Book
import com.example.swapup.data.model.Offer
import com.example.swapup.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


class FirestoreRepo(private val firestore: FirebaseFirestore ) {

    private val savedRef = firestore.collection("saved")
    private val offerRef = firestore.collection("offer")
    private val demandRef = firestore.collection("demand")

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
            val refer = savedRef
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

    suspend fun createOffer(offer: Offer): Boolean{
        return try {
            val newDoc = offerRef.document()
            val offerWithId = offer.copy(uid = newDoc.id)
            newDoc.set(offerWithId).await()
            val json = Gson().toJson(offerWithId.copy(photo = "null"))
            Log.d(TAG, json)
            Log.d(TAG,"Offer is saved!")
            true
        } catch (e: Exception){
            Log.d(TAG,"${e.localizedMessage}")
            false
        }
    }
    fun getOffers(): Flow<List<Offer>> = flow {
        try {
            val snapshot = offerRef
                .get()
                .await()
            val list = snapshot.documents.mapNotNull { it.toObject<Offer>() }
            emit(list)
        } catch (e: Exception){
            Log.w(TAG, "Error getting offers", e)
        }
    }
    suspend fun disactivateOffer(offer: Offer): Boolean{
        return try {
            val reference = offerRef.document(offer.uid)
            val disactivatedOffer = offer.copy(active = false)
            reference.set(disactivatedOffer).await()
            Log.d(TAG,"Offer is deactivated!")
            true
        } catch (e: Exception){
            Log.d(TAG,"${e.localizedMessage}")
            false
        }
    }
    suspend fun activateOffer(offer: Offer): Boolean{
        return try {
            val reference = offerRef.document(offer.uid)
            val disactivatedOffer = offer.copy(active = true)
            reference.set(disactivatedOffer).await()
            Log.d(TAG,"Offer is activated!")
            true
        } catch (e: Exception){
            Log.d(TAG,"${e.localizedMessage}")
            false
        }
    }
    suspend fun deleteOffer(offer: Offer): Boolean{
        return try{
            val result = offerRef.document(offer.uid).delete().await()
            Log.d(TAG,"Offer is deleted!")
            true
        } catch (e: Exception){
            Log.d(TAG,"${e.localizedMessage}")
            false
        }
    }

    companion object {
        private const val TAG = "FireStoreRepository"
    }
}