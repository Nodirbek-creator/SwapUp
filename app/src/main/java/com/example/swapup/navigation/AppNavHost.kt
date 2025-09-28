package com.example.swapup.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.swapup.MainActivity
import com.example.swapup.data.repository.FirebaseAuthRepo
import com.example.swapup.data.repository.FirestoreRepo
import com.example.swapup.data.sharedpref.DataManager
import com.example.swapup.ui.screens.CategoryScreen
import com.example.swapup.ui.screens.CommentScreen
import com.example.swapup.ui.screens.CreateDemand
import com.example.swapup.ui.screens.CreateOffer
import com.example.swapup.ui.screens.DemandInfo
import com.example.swapup.ui.screens.DemandScreen
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
import com.example.swapup.viewmodel.BookViewModel
import com.example.swapup.viewmodel.CreateDemandViewModel
import com.example.swapup.viewmodel.CreateOfferViewModel
import com.example.swapup.viewmodel.DemandInfoViewModel
import com.example.swapup.viewmodel.DemandViewModel
import com.example.swapup.viewmodel.InfoViewModel
import com.example.swapup.viewmodel.LoginViewModel
import com.example.swapup.viewmodel.MainViewModel
import com.example.swapup.viewmodel.OfferInfoViewModel
import com.example.swapup.viewmodel.OfferViewModel
import com.example.swapup.viewmodel.PdfViewModel
import com.example.swapup.viewmodel.ProfileViewModel
import com.example.swapup.viewmodel.SearchViewModel
import com.example.swapup.viewmodel.SignUpViewModel
import com.example.swapup.viewmodel.viewmodelFactory.CreateViewModelFactory
import java.net.URLDecoder

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    dataManager: DataManager,
    authRepository: FirebaseAuthRepo,
    firestoreRepo: FirestoreRepo,
    context: Context,
    activity: MainActivity,
    localLanguage: String,
    onLanguageChange:(String)->Unit
){
    val bookViewModel: BookViewModel = viewModel()
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
            val mainViewModel = MainViewModel(dataManager, bookViewModel)
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
                val demandVM: DemandViewModel = viewModel()
                MainScreen(
                    navController = navController,
                    vm = mainViewModel,
                    content = {
                        DemandScreen(
                            navController,
                            demandVM
                        )
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
                                onLanguageChange("en")
                            },
                            onUzbekClick = {
                                onLanguageChange("uz")
                            },
                            onRussianClick = {
                                onLanguageChange("ru")
                            }
                        )
                    }
                )
            }

            val infoViewModel = InfoViewModel(dataManager)
            val pdfViewModel = PdfViewModel(dataManager)
            composable("${Routes.Info.name}/{bookId}") {backStack ->
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
                ViewModelProvider(activity, CreateViewModelFactory(dataManager, context))[CreateOfferViewModel::class.java]
            CreateOffer(
                navController,
                offerViewModel
            )
        }
        composable(Routes.CreateDemand.name){
            val demandVM =
                ViewModelProvider(activity, CreateViewModelFactory(dataManager, context))[CreateDemandViewModel::class.java]
            CreateDemand(
                navController,
                demandVM
            )
        }
        composable("${Routes.OfferInfo.name}/{uid}"){ stackEntry->
            val uid = stackEntry.arguments?.getString("uid")
            val offerInfoVM = OfferInfoViewModel(dataManager, firestoreRepo, uid!!)
            OfferInfoScreen(
                navController,
                offerInfoVM
            )
        }
        composable("${Routes.DemandInfo.name}/{uid}"){ stackEntry->
            val uid = stackEntry.arguments?.getString("uid")
            val demandInfoVM = DemandInfoViewModel(dataManager, firestoreRepo, uid!!)
            DemandInfo(
                navController,
                demandInfoVM
            )
        }

        composable(Routes.Profile.name) {
            val profileViewModel = ProfileViewModel(dataManager)
            ProfileScreen(
                navController = navController,
                vm = profileViewModel,
            )
        }
    }
}