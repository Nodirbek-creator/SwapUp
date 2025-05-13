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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.swapup.connection.ConnectivityObserver
import com.example.swapup.connection.NetworkConnectivityObserver
import com.example.swapup.data.network.RetrofitInstance
import com.example.swapup.data.repository.AuthRepository
import com.example.swapup.data.repository.FirestoreRepo
import com.example.swapup.data.sharedpref.DataManager
import com.example.swapup.navigation.Routes
import com.example.swapup.ui.screens.CategoryScreen
import com.example.swapup.ui.screens.CommentScreen
import com.example.swapup.ui.screens.CreateOffer
import com.example.swapup.ui.screens.ErrorScreen
import com.example.swapup.ui.screens.HomeScreen
import com.example.swapup.ui.screens.InfoScreen
import com.example.swapup.ui.screens.LoginScreen
import com.example.swapup.ui.screens.MainScreen
import com.example.swapup.ui.screens.OfferInfoScreen
import com.example.swapup.ui.screens.OfferScreen
import com.example.swapup.ui.screens.PdfViewerScreenUrl
import com.example.swapup.ui.screens.ProfileScreen
import com.example.swapup.ui.screens.SearchScreen
import com.example.swapup.ui.screens.SettingsScreen
import com.example.swapup.ui.screens.SignUpScreen
import com.example.swapup.ui.theme.DarkBlue
import com.example.swapup.viewmodel.BookViewModel
import com.example.swapup.viewmodel.InfoViewModel
import com.example.swapup.viewmodel.LoginViewModel
import com.example.swapup.viewmodel.MainViewModel
import com.example.swapup.viewmodel.NetworkViewModel
import com.example.swapup.viewmodel.CreateOfferViewModel
import com.example.swapup.viewmodel.OfferInfoViewModel
import com.example.swapup.viewmodel.OfferViewModel
import com.example.swapup.viewmodel.PdfViewModel
import com.example.swapup.viewmodel.ProfileViewModel
import com.example.swapup.viewmodel.SearchViewModel
import com.example.swapup.viewmodel.SignUpViewModel
import com.example.swapup.viewmodel.viewmodelFactory.CreateOfferViewModelFactory
import com.example.swapup.viewmodel.viewmodelFactory.OfferInfoVMFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.yariksoffice.lingver.Lingver
import java.net.URLDecoder
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
            val bookViewModel: BookViewModel = viewModel()
//            Log.d("user","${dataManager.getUser()}")
//            val startDestination =
//                if(dataManager.userLogged()){ Routes.Main.name }
//                else {Routes.Login.name}
//            Log.d("currentUser","${dataManager.getUser()}")
            when(status.value) {
                null ->{
                    Box(modifier = Modifier.fillMaxSize()){
                        CircularProgressIndicator(
                            color = DarkBlue,
                            modifier = Modifier.align(Alignment.Center))
                    }
                }
                ConnectivityObserver.Status.Available->{
                    val authRepository = AuthRepository(RetrofitInstance.api, dataManager)
                    val fireStoreRepo = FirestoreRepo(FirebaseFirestore.getInstance())
                    NavHost(
                        navController = navController,
                        startDestination = Routes.Main.name
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
                            val mainViewModel = MainViewModel(dataManager, bookViewModel, navController)
                            composable(Routes.Home.name) {
                                MainScreen(
                                    navController = navController,
                                    vm = mainViewModel,
                                    content = {
                                        HomeScreen(
                                            navController = navController,
                                            bookVM = bookViewModel
                                        )
                                    }
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
                                )
                            }
                            composable("${Routes.Search.name}/{query}") { backStack ->
                                val query = backStack.arguments?.getString("query")
                                val searchViewModel = SearchViewModel(
                                    query?:"",
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
                                    }
                                )
                            }
                            composable(Routes.Offer.name) {
                                val offerViewModel: OfferViewModel = viewModel()
                                MainScreen(
                                    navController = navController,
                                    vm = mainViewModel,
                                    content = {
                                        OfferScreen(
                                            navController,
                                            offerViewModel
                                        )
                                    },
                                )
                            }
                            composable(Routes.Demand.name) {
                                MainScreen(
                                    navController = navController,
                                    vm = mainViewModel,
                                    content = {

                                    }
                                )
                            }
                            composable(Routes.Settings.name) {
                                MainScreen(
                                    navController = navController,
                                    vm = mainViewModel,
                                    content = {
                                        SettingsScreen(
                                            localLanguage = localLanguage,
                                            onEnglishClick = {
                                                changeLanguage("en")
                                            },
                                            onUzbekClick = {
                                                changeLanguage("uz")
                                            },
                                            onRussianClick = {
                                                changeLanguage("ru")
                                            }
                                        )
                                    }
                                )
                            }

                            val pdfViewModel = PdfViewModel(dataManager)
                            composable("${Routes.Info.name}/{bookId}") {backStack ->
                                val infoViewModel = InfoViewModel(dataManager)
                                val bookId = backStack.arguments?.getString("bookId")?.toInt()
                                InfoScreen(
                                    navController = navController,
                                    vm = infoViewModel,
                                    bookId = bookId!!
                                )
                            }
                            composable(Routes.Comment.name) {
                                CommentScreen(
                                    navController = navController
                                )
                            }

                            composable("${Routes.Pdf.name}/{bookId}/{pdfUrl}") { navBackStackEntry ->
                                val bookId = navBackStackEntry.arguments?.getString("bookId")?.toInt()?:1
                                val pdfUrl = URLDecoder.decode(navBackStackEntry.arguments?.getString("pdfUrl") ?: "", "UTF-8")
                                PdfViewerScreenUrl(
                                    navController = navController,
                                    vm = pdfViewModel,
                                    bookId = bookId,
                                    pdfUrl = pdfUrl
                                )
                            }
                        }
                        composable(Routes.CreateOffer.name){
                            val offerViewModel =
                                ViewModelProvider(this@MainActivity, CreateOfferViewModelFactory(dataManager, this@MainActivity))[CreateOfferViewModel::class.java]
                            CreateOffer(
                                navController,
                                offerViewModel
                            )
                        }
                        composable("${Routes.OfferInfo.name}/{uid}"){ stackEntry->
                            val uid = stackEntry.arguments?.getString("uid")
                            val offerInfoVM = OfferInfoViewModel(dataManager, fireStoreRepo, uid!!)
                            OfferInfoScreen(
                                navController,
                                offerInfoVM
                            )
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


