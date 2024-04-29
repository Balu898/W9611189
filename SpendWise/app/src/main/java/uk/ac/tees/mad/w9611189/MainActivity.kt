package uk.ac.tees.mad.w9611189

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.w9611189.ui.home.HomeScreen
import uk.ac.tees.mad.w9611189.ui.login.LoginScreen
import uk.ac.tees.mad.w9611189.ui.login.RegisterScreen
import uk.ac.tees.mad.w9611189.ui.profile.AddNewCategory
import uk.ac.tees.mad.w9611189.ui.profile.ProfileScreen
import uk.ac.tees.mad.w9611189.ui.splash.SplashScreen
import uk.ac.tees.mad.w9611189.ui.theme.SpendWiseTheme
import uk.ac.tees.mad.w9611189.ui.transactions.AllTransactions
import uk.ac.tees.mad.w9611189.ui.transactions.NewTransaction
import uk.ac.tees.mad.w9611189.ui.util.Routes


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpendWiseTheme {

                val navController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(

                    topBar = {
                        when (currentRoute) {
                            Routes.HomeScreen.route, Routes.AllTransactionScreen.route,Routes.NewTransactionScreen.route, Routes.ProfileScreen.route,
                            Routes.NewCategoryScreen.route -> {

                                TopAppBar(
                                    colors = TopAppBarDefaults.topAppBarColors(
                                        containerColor = colorResource(id = R.color.purple_200),
                                        titleContentColor = colorResource(id = R.color.white),
                                        actionIconContentColor = colorResource(id = R.color.white),
                                        navigationIconContentColor = colorResource(id = R.color.white)
                                    ),
                                    navigationIcon = {

                                        if (currentRoute == Routes.NewTransactionScreen.route ||
                                            currentRoute == Routes.NewCategoryScreen.route) {
                                            Icon(
                                                Icons.AutoMirrored.Filled.ArrowBack,
                                                modifier = Modifier
                                                    .padding(start = 8.dp)
                                                    .clickable {
                                                        navController.popBackStack()
                                                    },
                                                contentDescription = "Back Arrow"
                                            )
                                        }

                                    },
                                    title = {
                                        Text(
                                            text = currentRoute,
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    },
                                )
                            }

                            else -> {}
                        }
                    },
                    bottomBar = {

                        when (currentRoute) {
                            Routes.HomeScreen.route, Routes.AllTransactionScreen.route, Routes.ProfileScreen.route -> {
                                BottomNavigation(
                                    backgroundColor = colorResource(id = R.color.purple_200),
                                ) {


                                    BottomNavigationItem(
                                        modifier = Modifier.padding(top = 8.dp),
                                        selected = currentRoute == Routes.AllTransactionScreen.route,
                                        onClick = {
                                            if (currentRoute != Routes.AllTransactionScreen.route) {
                                                navController.navigate(Routes.AllTransactionScreen.route){
                                                    popUpTo(Routes.HomeScreen.route)
                                                }
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                Icons.Filled.Info,
                                                tint = Color.White,
                                                contentDescription = "Transactions"
                                            )
                                        },
                                        label = {
                                            Text(
                                                "Transactions", color = Color.White,
                                                modifier = Modifier.padding(vertical = 16.dp)
                                            )
                                        }
                                    )

                                    BottomNavigationItem(
                                        modifier = Modifier.padding(top = 8.dp),
                                        selected = currentRoute == Routes.HomeScreen.route,
                                        onClick = {
                                            if (currentRoute != Routes.HomeScreen.route) {
                                                navController.popBackStack()
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                Icons.Filled.Home,
                                                tint = Color.White,
                                                contentDescription = "Home"
                                            )
                                        },
                                        label = {
                                            Text(
                                                "Home", color = Color.White,
                                                modifier = Modifier.padding(vertical = 16.dp)
                                            )
                                        }
                                    )
                                    BottomNavigationItem(
                                        modifier = Modifier.padding(top = 8.dp),
                                        selected = currentRoute == Routes.ProfileScreen.route,
                                        onClick = {
                                            if (currentRoute != Routes.ProfileScreen.route) {
                                                navController.navigate(Routes.ProfileScreen.route){
                                                    popUpTo(Routes.HomeScreen.route)
                                                }
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                Icons.Filled.AccountBox,
                                                tint = Color.White,
                                                contentDescription = "Profile"
                                            )
                                        },
                                        label = {
                                            Text(
                                                "Profile", color = Color.White,
                                                modifier = Modifier.padding(vertical = 16.dp)
                                            )
                                        }
                                    )
                                }
                            }

                            else -> {

                            }

                        }

                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {

                        NavHost(
                            modifier = Modifier
                                .fillMaxSize(),
                            navController = navController,
                            startDestination = Routes.SplashScreen.route
                        ) {
                            composable(route = Routes.SplashScreen.route) {
                                SplashScreen(navController)
                            }
                            composable(route = Routes.HomeScreen.route) {
                                HomeScreen(navController)
                            }
                            composable(route = Routes.AllTransactionScreen.route) {
                                AllTransactions()
                            }
                            composable(route = Routes.ProfileScreen.route) {
                                ProfileScreen(navController)
                            }
                            composable(route = Routes.NewTransactionScreen.route) {
                                NewTransaction(navController)
                            }
                            composable(route = Routes.NewCategoryScreen.route) {
                                AddNewCategory()
                            }
                            composable(route = Routes.LoginScreen.route) {
                                LoginScreen(navController)
                            }
                            composable(route = Routes.RegisterScreen.route) {
                                RegisterScreen(navController)
                            }

                        }
                    }
                }
            }
        }
    }
}
