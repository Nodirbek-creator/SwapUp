package com.example.swapup.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapup.data.model.Language
import com.example.swapup.data.model.Offer
import com.example.swapup.data.repository.FirestoreRepo
import com.example.swapup.data.sharedpref.DataManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class CreateOfferViewModel(
    private val dataManager: DataManager,
    private val context: Context): ViewModel() {

    var title by mutableStateOf("")
        private set
    var author by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var isActive by mutableStateOf(true)
        private set
    var photoUri by mutableStateOf<Uri?>(null)
        private set
    var language by mutableStateOf(Language.Unspecified)
        private set

    var titleError by mutableStateOf<String?>(null)
        private set
    var authorError by mutableStateOf<String?>(null)
        private set
    var descriptionError by mutableStateOf<String?>(null)
        private set

    val publisher = dataManager.getUser().username

    private val repo = FirestoreRepo(FirebaseFirestore.getInstance())

    fun updateTitle(newTitle: String){
        title = newTitle
        titleError = null
    }
    fun updateAuthor(newAuthor: String){
        author = newAuthor
        authorError = null
    }
    fun updateDescription(newDescription: String){
        description = newDescription
        descriptionError = null
    }
    fun updatePhoto(newUri: Uri){
        photoUri = newUri
    }
    fun updateActive(newVal: Boolean){
        isActive = newVal
    }
    fun updateLanguage(newLang: Language){
        language = newLang
    }

    fun validateTitle(){
        titleError = if(title.isEmpty()) "Title field shouldn't be empty" else null
    }

    fun validateAuthor(){
        authorError = if(author.isEmpty()) "Author field shouldn't be empty" else null
    }

    fun validateDescription(){
        descriptionError = if(!hasFiveWords(description)) "Description should consist minimum of 5 words" else null
    }
    fun hasFiveWords(input: String): Boolean {
        val words = input.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }
        return words.size >= 5
    }
    fun isEverythingOk():Boolean{
        return titleError==null && authorError==null&& descriptionError==null
    }
    fun createOffer(){
        viewModelScope.launch {
            try {
                val offer = Offer(
                    title = title,
                    author = author,
                    photo = bitmapToBase64(photoUri!!, context),
                    description = description,
                    active = true,
                    language = Language.Russian,
                    publisher = publisher
                )
                val response = repo.createOffer(offer)
                Log.d("OfferVM","${offer.copy(photo = "")}")
            } catch (e: Exception){
                Log.d("OfferVM","${e.localizedMessage}")
            }
        }
    }
}
fun bitmapToBase64(uri: Uri, context: Context): String{
    val inputStream = context.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    val origHeight = bitmap.height
    val origWidth = bitmap.width
    val ratio = origWidth.toFloat()/ origHeight.toFloat()
    val newWidth = 800
    val newHeight = (newWidth/ratio).toInt()
    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false)
    val byteArrayOutputStream = ByteArrayOutputStream()
    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream) // 80 is the quality
    val byteArray = byteArrayOutputStream.toByteArray()
    val base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)
    return base64String

}