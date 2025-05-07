package com.example.swapup.navigation

import com.example.swapup.R

enum class Routes {
    Login,
    SignUp,
    Main,
    Home,
    Search,
    Articles,
    Saved,
    Settings,
    Profile,
    Category,
    Info,
    Comment,
    Pdf
}
sealed class Screen(val route: String, val icon: Int){
    object Home: Screen(Routes.Home.name, R.drawable.book)
    object Search: Screen(Routes.Search.name, R.drawable.search)
    object Articles: Screen(Routes.Articles.name, R.drawable.feather)
    object Saved: Screen(Routes.Saved.name, R.drawable.saved)
    object Settings: Screen(Routes.Settings.name, R.drawable.setting)
    object Category: Screen(Routes.Category.name, R.drawable.book)
}