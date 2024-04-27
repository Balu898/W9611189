package uk.ac.tees.mad.w9611189.ui.util

import okhttp3.Route

sealed class Routes(val route:String) {
    data object HomeScreen : Routes("SpendWise")
    data object SplashScreen : Routes("Splash Screen")
    data object ProfileScreen : Routes("Profile")
    data object RegisterScreen : Routes("Register")
    data object LoginScreen : Routes("Login")
    data object AllTransactionScreen : Routes("All Transaction")
    data object NewTransactionScreen : Routes("New Transaction")
    data object NewCategoryScreen : Routes("Add Category")
}