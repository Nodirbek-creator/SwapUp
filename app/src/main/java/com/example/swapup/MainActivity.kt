package com.example.swapup

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.swapup.connection.ConnectivityObserver
import com.example.swapup.connection.NetworkConnectivityObserver
import com.example.swapup.data.network.RetrofitInstance
import com.example.swapup.data.repository.AuthRepository
import com.example.swapup.data.sharedpref.DataManager
import com.example.swapup.navigation.Routes
import com.example.swapup.ui.screens.CategoryScreen
import com.example.swapup.ui.screens.CommentScreen
import com.example.swapup.ui.screens.ErrorScreen
import com.example.swapup.ui.screens.HomeScreen
import com.example.swapup.ui.screens.InfoScreen
import com.example.swapup.ui.screens.LoginScreen
import com.example.swapup.ui.screens.MainScreen
import com.example.swapup.ui.screens.PdfViewerScreenUrl
import com.example.swapup.ui.screens.ProfileScreen
import com.example.swapup.ui.screens.SearchScreen
import com.example.swapup.ui.screens.SignUpScreen
import com.example.swapup.viewmodel.BookViewModel
import com.example.swapup.viewmodel.FireStoreViewModel
import com.example.swapup.viewmodel.LoginViewModel
import com.example.swapup.viewmodel.MainViewModel
import com.example.swapup.viewmodel.PdfViewModel
import com.example.swapup.viewmodel.ProfileViewModel
import com.example.swapup.viewmodel.SearchViewModel
import com.example.swapup.viewmodel.SignUpViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val connectivityObserver = NetworkConnectivityObserver(this)
        val dataManager = DataManager(this)
        setContent {
            val status = connectivityObserver.observe()
                .collectAsState(initial = ConnectivityObserver.Status.Available)
            val navController = rememberNavController()
            val startDestination =
                if(dataManager.userLogged()){ Routes.Main.name }
                else {Routes.Login.name}
            Log.d("currentUser","${dataManager.getUser()}")
            when(status.value) {
                ConnectivityObserver.Status.Available->{
                    val authRepository = AuthRepository(RetrofitInstance.api, dataManager)
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable(Routes.Login.name) {
                            val loginViewModel = LoginViewModel(authRepository)
                            LoginScreen(navController, loginViewModel)
                        }
                        composable(Routes.SignUp.name) {
                            val signUpViewModel = SignUpViewModel(authRepository)
                            SignUpScreen(navController, signUpViewModel)
                        }
                        navigation(startDestination = Routes.Home.name, route = Routes.Main.name) {
                            val bookViewModel = BookViewModel()
                            val mainViewModel = MainViewModel(dataManager, navController)
                            val fireStoreViewModel = FireStoreViewModel(
                                dataManager = dataManager,
                                bookViewModel = bookViewModel
                            )
                            composable(Routes.Home.name) {
                                MainScreen(
                                    navController = navController,
                                    vm = mainViewModel,
                                    content = {
                                        HomeScreen(
                                            navController = navController,
                                            bookVM = bookViewModel
                                        )
                                    },
                                    bookVM = bookViewModel
                                )
                            }
                            composable(Routes.Category.name) {
                                MainScreen(
                                    navController = navController,
                                    vm = mainViewModel,
                                    content = {
                                        CategoryScreen(
                                            navController,
                                            bookVM = bookViewModel
                                        )
                                    },
                                    bookVM = bookViewModel
                                )
                            }
                            composable(Routes.Search.name) {
                                val searchViewModel = SearchViewModel(
                                    "",
                                    dataManager
                                )
                                MainScreen(
                                    navController = navController,
                                    vm = mainViewModel,
                                    content = {
                                        SearchScreen(
                                            navController = navController,
                                            vm = searchViewModel
                                        )
                                    },
                                    bookVM = bookViewModel
                                )
                            }
                            composable(Routes.Offer.name) {
                                MainScreen(
                                    navController = navController,
                                    vm = mainViewModel,
                                    content = {
                                    },
                                    bookVM = bookViewModel
                                )
                            }
                            composable(Routes.Demand.name) {
                                MainScreen(
                                    navController = navController,
                                    vm = mainViewModel,
                                    content = {

                                    },
                                    bookVM = bookViewModel
                                )
                            }
                            composable(Routes.Settings.name) {
                                MainScreen(
                                    navController = navController,
                                    vm = mainViewModel,
                                    content = {

                                    },
                                    bookVM = bookViewModel
                                )
                            }

                            composable(Routes.Info.name) {
                                val pdfViewModel = PdfViewModel()
                                val fireStoreViewModel = FireStoreViewModel(dataManager = dataManager, bookViewModel = bookViewModel)
                                InfoScreen(
                                    navController = navController,
                                    vm = fireStoreViewModel,
                                    pdfVM = pdfViewModel,
                                    bookVM = bookViewModel
                                )
                            }
                            composable(Routes.Comment.name) {
                                CommentScreen(
                                    navController = navController
                                )
                            }
                            composable(Routes.Pdf.name){
                                val pdfViewModel = PdfViewModel()
                                PdfViewerScreenUrl(
                                    navController = navController,
                                    vm = pdfViewModel
                                )
                            }
                        }

                        composable(Routes.Profile.name) {
                            val profileViewModel = ProfileViewModel(dataManager,)
                            val bookViewModel = BookViewModel()
                            ProfileScreen(
                                navController = navController,
                                vm = profileViewModel,
                                bookVM = bookViewModel
                            )
                        }

                    }
                }
                ConnectivityObserver.Status.Losing->{
                    ErrorScreen(
                        R.drawable.wifi_warning,
                        text = "Warning! Your connection is unstable"
                    )
                }
                ConnectivityObserver.Status.Lost->{
                    ErrorScreen(
                        R.drawable.wifi_lost,
                        text = "Internet connection is lost"
                    )
                }
                ConnectivityObserver.Status.Unavailable->{
                    ErrorScreen(
                        R.drawable.no_connection,
                        text = "No network available.\nPlease try again"
                    )
                }
            }
        }
    }
}
