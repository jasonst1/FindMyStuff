package com.anakbaikbaik.findmystuff.Navigation

sealed class Screen(val route: String){
    object HomeScreen : Screen("home_screen")
    object EditScreen : Screen("edit_screen")
}