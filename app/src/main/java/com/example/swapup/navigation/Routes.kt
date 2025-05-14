package com.example.swapup.navigation

import com.example.swapup.R

enum class Routes {
    Login,
    SignUp,
    Main,
    Home,
    Search,
    Offer,
    Demand,
    Settings,
    Profile,
    Category,
    Info,
    Comment,
    Pdf,
    CreateOffer,
    OfferInfo,
    CreateDemand,
    DemandInfo,
}
sealed class Screen(val route: String, val icon: Int){
    object Home: Screen(Routes.Home.name, R.drawable.book)
    object Search: Screen(Routes.Search.name, R.drawable.search)
    object Offer: Screen(Routes.Offer.name, R.drawable.offer)
    object Demand: Screen(Routes.Demand.name, R.drawable.demand)
    object Settings: Screen(Routes.Settings.name, R.drawable.setting)
    object Category: Screen(Routes.Category.name, R.drawable.book)
}