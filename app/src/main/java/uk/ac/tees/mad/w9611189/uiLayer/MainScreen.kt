package uk.ac.tees.mad.w9611189.uiLayer

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uk.ac.tees.mad.w9611189.route.Routes
import uk.ac.tees.mad.w9611189.viewModel.UserSignupViewModel

@Composable
fun MainScreen(navController: NavHostController) {

    val userSignupViewModel: UserSignupViewModel = viewModel()
    NavHost(navController = navController, startDestination = Routes.Register.name) {
        composable(route = Routes.Register.name) {
            RegisterScreen(userSignupViewModel,navController = navController)
        }

        composable(route = Routes.Login.name) {
            LoginScreen(navController = navController)
        }

        composable(route = Routes.Home.name) {
            HomeScreen(navController = navController)
        }
    }
}