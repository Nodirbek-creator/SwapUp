package com.example.swapup.viewmodel

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Geocoder
import android.location.Location
import android.media.ExifInterface
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapup.data.model.Language
import com.example.swapup.data.model.Offer
import com.example.swapup.data.repository.FirestoreRepo
import com.example.swapup.data.sharedpref.DataManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Locale

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
    var location by mutableStateOf("Beruniy shoh ko'chasi, Тоshkent, Toshkent Viloyati, Узбекистан")
        private set
    var longitude by mutableDoubleStateOf(0.00)
        private set
    var latitude by mutableDoubleStateOf(0.00)
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
    var photoError by mutableStateOf<String?>(null)
        private set

    val owner = dataManager.getUser().username

    private val repo = FirestoreRepo(FirebaseFirestore.getInstance())

    val hasLocationPermission = mutableStateOf(ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED)

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
        photoError = null
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
    fun validatePhoto(){
        photoError = if(photoUri==null) "Please upload the image of the book" else null
    }
    fun hasFiveWords(input: String): Boolean {
        val words = input.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }
        return words.size >= 5
    }
    fun isEverythingOk():Boolean{
        return titleError==null && authorError==null&& descriptionError==null && photoError==null
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
                    language = language.name,
                    owner = owner
                )
                repo.createOffer(offer)
            } catch (e: Exception){
                Log.d("CreateOfferVM","${e.localizedMessage}")
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

    fun getCoordinates(){
        viewModelScope.launch {
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val result = geocoder.getFromLocationName(location, 1)
                if(!result.isNullOrEmpty()){
                    latitude = result[0].latitude
                    longitude = result[0].longitude
                }
            }catch (e: Exception){
                Log.d(TAG, "getCoordinates: ${e.message}")
            }
        }
    }

    fun getLocation(){
        viewModelScope.launch {
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val result = geocoder.getFromLocation(latitude, longitude, 1)
                if(!result.isNullOrEmpty()){
                    location = result[0].getAddressLine(0)
                }
            }catch (e: Exception){
                Log.d(TAG, "getCoordinates: ${e.message}")
            }
        }
    }

    fun getOneShotLocation(onResult: (Location?) -> Unit) {
        val fusedClient = LocationServices.getFusedLocationProviderClient(context)

        val cts = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            onResult(null); return
        }

        fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
            .addOnSuccessListener { loc: Location? ->
                loc?.let {
                    longitude = loc.longitude
                    latitude = loc.latitude
                    getLocation()
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}
fun bitmapToBase64(uri: Uri?, context: Context): String {
    uri?.let {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return ""

        val bytes = inputStream.readBytes()
        inputStream.close()

        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return ""

        val orientation = try {
            val exif = ExifInterface(ByteArrayInputStream(bytes))
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        } catch (e: Exception) {
            ExifInterface.ORIENTATION_NORMAL
        }

        val rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
            else -> bitmap
        }

        val ratio = rotatedBitmap.width.toFloat() / rotatedBitmap.height
        val resizedBitmap = Bitmap.createScaledBitmap(rotatedBitmap, 800, (800 / ratio).toInt(), false)

        return ByteArrayOutputStream().use { stream ->
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
        }
    }
    return ""
}

fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(angle) }
    return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
}
