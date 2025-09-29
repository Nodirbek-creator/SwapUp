package com.example.swapup.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapup.data.model.Demand
import com.example.swapup.data.model.Language
import com.example.swapup.data.repository.FirestoreRepo
import com.example.swapup.data.sharedpref.DataManager
import com.example.swapup.viewmodel.state.UiState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class CreateDemandViewModel(
    private val dataManager: DataManager,
    private val context: Context, ): ViewModel() {
    var title by mutableStateOf("")
        private set
    var author by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var isActive by mutableStateOf(true)
        private set
    var location by mutableStateOf("")
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

    private val repo = FirestoreRepo(FirebaseFirestore.getInstance())
    val owner = dataManager.getUser().username

    private val _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState = _uiState.value

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
    fun updateLanguage(newLang: Language){
        language = newLang
    }
    fun updateLocation(newLocation: String){
        location = newLocation
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

    fun createDemand(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val demand = Demand(
                    title = title,
                    author = author,
                    language = language.name,
                    photo = bitmapToBase64(photoUri, context) ,
                    description = description,
                    active = true,
                    owner = owner
                )
                val error = repo.createDemand(demand)
                Log.d("CreateDemandVM","${error}")
                if(error == null){
                    _uiState.value = UiState.Success
                }
                else{
                    _uiState.value = UiState.Error(error)
                }
            } catch (e: Exception){
                _uiState.value = UiState.Error("${e.localizedMessage}")
                Log.d("CreateDemandVM","${e.localizedMessage}")
            }
        }
    }

    fun clearAllFields(){
        updateTitle("")
        updateAuthor("")
        photoUri = null
        updateDescription("")
        updateLanguage(Language.Unspecified)
    }
}