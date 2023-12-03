package com.anakbaikbaik.findmystuff.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anakbaikbaik.findmystuff.Screens.AddScreen
import com.anakbaikbaik.findmystuff.Screens.ArchiveScreen
import com.anakbaikbaik.findmystuff.Screens.DeleteScreen
import com.anakbaikbaik.findmystuff.Screens.EditScreen
import com.anakbaikbaik.findmystuff.Screens.ForgetPasswordScreen
import com.anakbaikbaik.findmystuff.Screens.HomeScreen
import com.anakbaikbaik.findmystuff.Screens.LandingScreen
import com.anakbaikbaik.findmystuff.Screens.SignUpScreen
import com.anakbaikbaik.findmystuff.ViewModel.AuthViewModel

@Composable
fun Navigation(
    viewModel: AuthViewModel
) {
    val viewModel: AuthViewModel? = viewModel
    val firestoreViewModel: AuthViewModel? = viewModel

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.LandingScreen.route){
        composable(route = Screen.LandingScreen.route){
            LandingScreen(viewModel = viewModel, navController = navController)
        }
        composable(route = Screen.HomeScreen.route){
            HomeScreen(viewModel = viewModel, navController = navController, firestoreViewModel = firestoreViewModel)
        }
        composable(route = Screen.ArchiveScreen.route){
            ArchiveScreen(viewModel = viewModel, navController = navController, firestoreViewModel = firestoreViewModel)
        }
        composable(route = Screen.EditScreen.route){
             EditScreen(viewModel = viewModel, navController = navController)
        }
        composable(route = Screen.AddScreen.route) {
            AddScreen(viewModel = viewModel, navController = navController)
        }
        composable(route = Screen.SignUpScreen.route){
            SignUpScreen(viewModel = viewModel, navController = navController)
        }
        composable(route = Screen.ForgetPasswordScreen.route){
            ForgetPasswordScreen(viewModel = viewModel, navController = navController)
        }
        composable(
            route = "${Screen.DeleteScreen.route}/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            DeleteScreen(viewModel = viewModel, itemId = itemId, navController = navController)
        }

    }
}