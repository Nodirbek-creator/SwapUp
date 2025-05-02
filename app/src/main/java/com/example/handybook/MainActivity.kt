package com.example.handybook

import android.content.Context
import android.os.Bundle
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
import com.example.handybook.data.sharedpref.DataManager
import com.example.handybook.navigation.Routes
import com.example.handybook.screens.CategoryScreen
import com.example.handybook.screens.CommentScreen
import com.example.handybook.screens.ErrorScreen
import com.example.handybook.screens.HomeScreen
import com.example.handybook.screens.InfoScreen
import com.example.handybook.screens.LoginScreen
import com.example.handybook.screens.MainScreen
import com.example.handybook.screens.ProfileScreen
import com.example.handybook.screens.SignUpScreen
import com.example.handybook.viewmodel.AuthViewModel
import com.example.handybook.viewmodel.BookViewModel
import com.example.handybook.viewmodel.LoginViewModel
import com.example.handybook.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val connectivityObserver = NetworkConnectivityObserver(this)
        setContent {
            val status = connectivityObserver.observe().collectAsState(
                initial = ConnectivityObserver.Status.Unavailable
            )
            val navController = rememberNavController()
            val sharedPreferences = this@MainActivity.getPreferences(Context.MODE_PRIVATE)

            when(status.value) {
                ConnectivityObserver.Status.Available->{
                    val dataManager = DataManager(sharedPreferences)
                    val authViewModel = AuthViewModel()
                    val mainViewModel = MainViewModel(dataManager, navController)
                    val bookViewModel = BookViewModel()
                    NavHost(
                        navController = navController,
                        startDestination = Routes.Login.name
                    ) {
                        composable(Routes.Login.name) {
                            val loginViewModel = LoginViewModel()
                            LoginScreen(navController, loginViewModel)
                        }
                        composable(Routes.SignUp.name) {
                            SignUpScreen(navController, authViewModel)
                        }
                        navigation(startDestination = Routes.Home.name, route = Routes.Main.name) {
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
                            ProfileScreen(
                                navController = navController,
                                authVM = authViewModel,
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
