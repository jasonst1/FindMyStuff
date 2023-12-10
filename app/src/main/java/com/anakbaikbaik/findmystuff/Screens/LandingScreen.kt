package com.anakbaikbaik.findmystuff.Screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.anakbaikbaik.findmystuff.Data.Resource
import com.anakbaikbaik.findmystuff.Navigation.Screen
import com.anakbaikbaik.findmystuff.R
import com.anakbaikbaik.findmystuff.ViewModel.AuthViewModel
import com.anakbaikbaik.findmystuff.ui.theme.topBar

@Composable
fun LandingScreen(navController: NavController, viewModel: AuthViewModel?){
    Log.d("ForgetPasswordDebug", "LandingScreen")
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = { topBar() },
            content = {it
                // Add padding to the main content area
                LandingArea(viewModel = viewModel, navController = navController)
            }
        )
    }
}

@Composable
fun LandingArea(viewModel: AuthViewModel?, navController: NavController){
        val authResource = viewModel?.loginFlow?.collectAsState()

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ClickableText(
                text = AnnotatedString("Sign up"),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp),
                onClick = {
                    navController.navigate(Screen.SignUpScreen.route)
                },
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Default,
                    textDecoration = TextDecoration.Underline,
                )
            )
        }
        Column(
            modifier = Modifier.padding(top = 150.dp, bottom = 50.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val email = remember { mutableStateOf(TextFieldValue()) }
            val password = remember { mutableStateOf(TextFieldValue()) }

//            Text(
//                text = "Log In",
////                style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Cursive)
//                style = TextStyle(
//                    fontSize = 40.sp,
//                    fontFamily = FontFamily.SansSerif,
//                    fontWeight = FontWeight.Bold,
//                    color = warnaUMN
//                )
//            )

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(40.dp))

            TextField(
                label = { Text(text = "email") },
                value = email.value,
                onValueChange = { email.value = it })

            Spacer(modifier = Modifier.height(40.dp))

            TextField(
                label = { Text(text = "Password") },
                value = password.value,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { password.value = it })

            Spacer(modifier = Modifier.height(40.dp))

            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {
                        viewModel?.loginUser(email.value.text, password.value.text)
                    },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Login")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            ClickableText(
                text = AnnotatedString("Forgot password?"),
                onClick = {
                    navController.navigate(Screen.ForgetPasswordScreen.route)
                },
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Default
                )
            )

            authResource?.value?.let{
                when(it){
                    is Resource.Failure -> {
                        val context = LocalContext.current
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                    }
                    Resource.Loading -> {
                        CircularProgressIndicator()
                    }
                    is Resource.Success -> {
                        LaunchedEffect(Unit) {
                            navController.navigate(Screen.HomeScreen.route) {
                                popUpTo(Screen.HomeScreen.route) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            }
        }
}