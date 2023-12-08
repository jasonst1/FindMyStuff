package com.anakbaikbaik.findmystuff.NavBars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.anakbaikbaik.findmystuff.Navigation.Screen
import com.anakbaikbaik.findmystuff.R
import com.anakbaikbaik.findmystuff.ViewModel.AuthViewModel
import com.anakbaikbaik.findmystuff.ui.theme.White
import com.anakbaikbaik.findmystuff.ui.theme.warnaUMN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithLogout(viewModel: AuthViewModel?, navController: NavController) {
    TopAppBar(
        title = {
            Text(
                stringResource(id = R.string.app_name),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = White,
                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(13.dp) // Add padding here
            )
        },
        actions = {
            // If you have any additional actions, you can add them here
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = warnaUMN)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Pushes the button to the end
        Spacer(modifier = Modifier.weight(0.1f))
        Button(
            onClick = {
                viewModel?.logout()
                navController.navigate(Screen.LandingScreen.route) {
                    popUpTo(Screen.LandingScreen.route) { inclusive = true }
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .width(100.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(text = "Log Out", style = MaterialTheme.typography.labelMedium)
        }
    }
}