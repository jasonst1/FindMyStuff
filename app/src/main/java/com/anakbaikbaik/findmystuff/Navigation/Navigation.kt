package com.anakbaikbaik.findmystuff.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anakbaikbaik.findmystuff.Screens.AddScreen
import com.anakbaikbaik.findmystuff.Screens.ArchiveScreen
import com.anakbaikbaik.findmystuff.Screens.DeleteScreen
import com.anakbaikbaik.findmystuff.Screens.EditScreen
import com.anakbaikbaik.findmystuff.Screens.ForgetPasswordScreen
import com.anakbaikbaik.findmystuff.Screens.HomeScreen
import com.anakbaikbaik.findmystuff.Screens.LandingScreen
import com.anakbaikbaik.findmystuff.Screens.SignUpScreen
import com.anakbaikbaik.findmystuff.ViewModel.AuthViewModel
import com.anakbaikbaik.findmystuff.ViewModel.RoleViewModel

@Composable
fun Navigation(
    viewModel: AuthViewModel,
    roleViewModel: RoleViewModel
) {
    val viewModel: AuthViewModel? = viewModel
    val firestoreViewModel: AuthViewModel? = viewModel
    val roleViewModel: RoleViewModel = roleViewModel

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.LandingScreen.route){
        composable(route = Screen.LandingScreen.route){
            LandingScreen(viewModel = viewModel, navController = navController)
        }
        composable(route = Screen.HomeScreen.route){
            HomeScreen(viewModel = viewModel, navController = navController, firestoreViewModel = firestoreViewModel, roleViewModel = roleViewModel)
        }
        composable(route = Screen.ArchiveScreen.route){
            ArchiveScreen(viewModel = viewModel, navController = navController, firestoreViewModel = firestoreViewModel)
        }
        composable(route = Screen.EditScreen.route){
             EditScreen(navController = navController)
        }
        composable(route = Screen.AddScreen.route) {
            AddScreen(navController = navController)
        }
        composable(route = Screen.SignUpScreen.route){
            SignUpScreen(viewModel = viewModel, navController = navController)
        }
        composable(route = Screen.ForgetPasswordScreen.route){
            ForgetPasswordScreen(viewModel = viewModel, navController = navController)
        }
        composable(route = Screen.DeleteScreen.route){
            DeleteScreen(navController = navController)
        }
    }
}