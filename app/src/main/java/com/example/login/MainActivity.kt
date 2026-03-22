package com.example.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.login.ui.screens.LoginScreen
import com.example.login.ui.screens.PersonalDataScreen
import com.example.login.ui.screens.ProfileScreen
import com.example.login.ui.screens.RegistrationScreen
import com.example.login.ui.theme.LOGINTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LOGINTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("personal_data")
                },
                onNavigateToRegister = {
                    navController.navigate("registration")
                }
            )
        }
        composable("registration") {
            RegistrationScreen(onRegisterSuccess = {
                navController.navigate("login")
            })
        }
        composable("personal_data") {
            PersonalDataScreen(
                name = "Andres Ramos Flores",
                matricula = "24393165",
                career = "Ingenieria",
                group = "LITIID42",
                onNavigateToProfile = {
                    navController.navigate("profile")
                }
            )
        }
        composable("profile") {
            ProfileScreen()
        }
    }
}
