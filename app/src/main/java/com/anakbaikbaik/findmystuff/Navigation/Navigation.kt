package com.anakbaikbaik.findmystuff.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anakbaikbaik.findmystuff.Screens.EditScreen
import com.anakbaikbaik.findmystuff.Screens.HomeScreen
import com.anakbaikbaik.findmystuff.Screens.LandingScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.LandingScreen.route){
        composable(route = Screen.LandingScreen.route){
            LandingScreen(navController = navController)
        }
        composable(route = Screen.HomeScreen.route){
            HomeScreen(navController = navController)
        }
        composable(route = Screen.EditScreen.route){
             EditScreen(navController = navController)
        }
    }
}