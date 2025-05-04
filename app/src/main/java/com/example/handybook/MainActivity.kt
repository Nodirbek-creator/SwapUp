package com.example.handybook

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
import com.example.handybook.connection.ConnectivityObserver
import com.example.handybook.connection.NetworkConnectivityObserver
import com.example.handybook.data.network.RetrofitInstance
import com.example.handybook.data.repository.AuthRepository
import com.example.handybook.data.sharedpref.DataManager
import com.example.handybook.navigation.Routes
import com.example.handybook.ui.screens.CategoryScreen
import com.example.handybook.ui.screens.CommentScreen
import com.example.handybook.ui.screens.ErrorScreen
import com.example.handybook.ui.screens.HomeScreen
import com.example.handybook.ui.screens.InfoScreen
import com.example.handybook.ui.screens.LoginScreen
import com.example.handybook.ui.screens.MainScreen
import com.example.handybook.ui.screens.ProfileScreen
import com.example.handybook.ui.screens.SignUpScreen
import com.example.handybook.viewmodel.BookViewModel
import com.example.handybook.viewmodel.LoginViewModel
import com.example.handybook.viewmodel.MainViewModel
import com.example.handybook.viewmodel.ProfileViewModel
import com.example.handybook.viewmodel.SignUpViewModel

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
                            composable(Routes.Home.name) {
                                MainScreen(
                                    navController = navController,
                                    vm = mainViewModel,
                                    content = {
                                        HomeScreen(
                                            navController = navController,
                                            bookVM = bookViewModel,
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
                                MainScreen(
                                    navController = navController,
                                    vm = mainViewModel,
                                    content = {

                                    },
                                    bookVM = bookViewModel
                                )
                            }
                            composable(Routes.Articles.name) {
                                MainScreen(
                                    navController = navController,
                                    vm = mainViewModel,
                                    content = {
                                    },
                                    bookVM = bookViewModel
                                )
                            }
                            composable(Routes.Saved.name) {
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
                                InfoScreen(
                                    navController = navController,
                                    bookVM = bookViewModel
                                )
                            }
                            composable(Routes.Comment.name) {
                                CommentScreen(
                                    navController
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
