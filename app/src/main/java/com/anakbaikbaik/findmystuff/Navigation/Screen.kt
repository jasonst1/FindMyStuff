package com.anakbaikbaik.findmystuff.Navigation

sealed class Screen(val route: String){
    object LandingScreen : Screen("landing_screen")
    object HomeScreen : Screen("home_screen")
    object ArchiveScreen : Screen("archive_screen")
    object EditScreen : Screen("edit_screen")
    object AddScreen : Screen("add_screen")
    object SignUpScreen : Screen("sign_up_screen")
    object ForgetPasswordScreen : Screen("forget_password_screen")
}