package com.example.swapup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.example.swapup.connection.ConnectivityObserver
import com.example.swapup.connection.NetworkConnectivityObserver
import com.example.swapup.data.repository.FirebaseAuthRepo
import com.example.swapup.data.repository.FirestoreRepo
import com.example.swapup.data.sharedpref.DataManager
import com.example.swapup.navigation.AppNavHost
import com.example.swapup.navigation.Routes
import com.example.swapup.ui.screens.ErrorScreen
import com.example.swapup.ui.theme.DarkBlue
import com.example.swapup.viewmodel.NetworkViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.yariksoffice.lingver.Lingver
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val localLanguage = Locale.getDefault().language
        Lingver.getInstance().setLocale(this, localLanguage)
        val connectivityObserver = NetworkConnectivityObserver(this)
        val networkViewModel = NetworkViewModel(connectivityObserver)
        val dataManager = DataManager(this)

        setContent {
            val status = networkViewModel.networkStatus.collectAsState()
            val navController = rememberNavController()

            val startDestination =
                if(dataManager.userLogged()){ Routes.Main.name }
                else {Routes.Login.name}

            when(status.value) {
                null ->{
                    Box(modifier = Modifier.fillMaxSize()){
                        CircularProgressIndicator(
                            color = DarkBlue,
                            modifier = Modifier.align(Alignment.Center))
                    }
                }
                ConnectivityObserver.Status.Available->{
                    val authRepository = FirebaseAuthRepo(FirebaseFirestore.getInstance(), dataManager, this)
                    val fireStoreRepo = FirestoreRepo(FirebaseFirestore.getInstance())
                    AppNavHost(
                        navController = navController,
                        startDestination = startDestination,
                        dataManager = dataManager,
                        authRepository = authRepository,
                        firestoreRepo = fireStoreRepo,
                        context = this,
                        activity = this,
                        localLanguage = localLanguage
                    ) {language ->
                        changeLanguage(language)
                    }
                }
                ConnectivityObserver.Status.Losing->{
                    ErrorScreen(
                        R.drawable.wifi_warning,
                        text = stringResource(R.string.network_losing)
                    )
                }
                ConnectivityObserver.Status.Lost->{
                    ErrorScreen(
                        R.drawable.wifi_lost,
                        text = stringResource(R.string.network_lost)
                    )
                }
                ConnectivityObserver.Status.Unavailable->{
                    ErrorScreen(
                        R.drawable.no_connection,
                        text = stringResource(R.string.network_unavailable)
                    )
                }
            }
        }
    }

    private fun changeLanguage(language: String){
        Lingver.getInstance().setLocale(this, language)
        recreate()
    }
}


