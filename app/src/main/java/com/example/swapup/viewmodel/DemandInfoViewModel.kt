package com.example.swapup.viewmodel

import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapup.R
import com.example.swapup.data.model.Demand
import com.example.swapup.data.model.Language
import com.example.swapup.data.model.LanguageBox
import com.example.swapup.data.repository.FirestoreRepo
import com.example.swapup.data.sharedpref.DataManager
import com.example.swapup.viewmodel.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class DemandInfoViewModel(
    private val dataManager: DataManager,
    private val repo: FirestoreRepo,
    private val uid: String,
    private val context: Context
): ViewModel() {
    private val currentUser = dataManager.getUser()

    private val _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState: UiState get() = _uiState.value

    var languageBox by mutableStateOf<LanguageBox?>(null)
    var location by mutableStateOf("Beruniy shoh ko'chasi, Тоshkent, Toshkent Viloyati, Узбекистан")
        private set
    var secretCode by mutableStateOf("")
        private set
    var secretCodeError by mutableStateOf<String?>(null)
        private set
    var message by mutableStateOf<String?>(null)
        private set

    init {
        getDemandById()
    }

    private val _demand = MutableLiveData<Demand>()
    val demand: LiveData<Demand> get() = _demand

    fun updateMessage(newMsg: String){
        viewModelScope.launch {
            message = newMsg
            delay(1000)
            message = null
        }
    }

    fun changeSecretCode(newCode: String){
        secretCode = newCode
        message = null
        secretCodeError = null
    }

    fun checkSecretCode(){
        if(secretCode!=demand.value!!.uid){
            message = "Secret code didn't match!"
            secretCodeError = "Wrong secret code"
        }
        else{
            disactiveDemand()
        }
    }

    fun disactiveDemand(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = repo.disactivateDemand(demand.value!!)
            if(result == null){
                _uiState.value = UiState.Success
                secretCodeError = null
                message = "Secret code applied successfully!"
            }
            else{
                _uiState.value = UiState.Error(result)
            }
        }
    }

    fun isOwner(): Boolean{
        return currentUser.username == demand.value?.owner
    }

    fun isNotOwner(): Boolean{
        return currentUser.username != demand.value?.owner
    }

    fun getDemandById(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = repo.getDemandById(uid)
            if(result == null){
                _uiState.value = UiState.Error("Couldn't find the offer")
            }
            else{
                _demand.value = result!!
                getLocation()
                languageBox = when(result.language){
                    Language.Uzbek.name ->{
                        LanguageBox(R.drawable.uzb, R.string.uzbek,)
                    }
                    Language.English.name ->{
                        LanguageBox(R.drawable.eng, R.string.english,)
                    }
                    Language.Russian.name->{
                        LanguageBox(R.drawable.ru, R.string.russian,)
                    }
                    else ->{
                        LanguageBox(R.drawable.unspecified, R.string.unspecified)
                    }
                }
                _uiState.value = UiState.Idle
            }
        }
    }

    fun launchTelegram(context: Context){
        val username = demand.value?.owner ?: ""
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://t.me/${username.removePrefix("@")}")
            setPackage("org.telegram.messenger") // Forces opening in Telegram app
        }

        // Fallback in case Telegram app is not installed
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Open in browser if Telegram is not installed
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/${username.removePrefix("@")}"))
            context.startActivity(browserIntent)
        }
    }

    fun getLocation(){
        viewModelScope.launch {
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val result = demand.value?.let { geocoder.getFromLocation(it.latitude, it.longitude, 1) }
                if(!result.isNullOrEmpty()){
                    location = result[0].getAddressLine(0)
                }
            }catch (e: Exception){
                Log.d(TAG, "getCoordinates: ${e.message}")
            }
        }
    }
}