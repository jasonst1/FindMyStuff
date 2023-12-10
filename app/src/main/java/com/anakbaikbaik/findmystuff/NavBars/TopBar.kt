package com.anakbaikbaik.findmystuff.NavBars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.anakbaikbaik.findmystuff.Navigation.Screen
import com.anakbaikbaik.findmystuff.R
import com.anakbaikbaik.findmystuff.ViewModel.AuthViewModel
import com.anakbaikbaik.findmystuff.ui.theme.White
import com.anakbaikbaik.findmystuff.ui.theme.warnaUMN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithLogout(viewModel: AuthViewModel?, navController: NavController) {
    var expanded by remember { mutableStateOf(false) }

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
            IconButton(
                onClick = { expanded = true }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(text = "Logout")},
                    onClick = { viewModel?.logout()
                    navController.navigate(Screen.LandingScreen.route) {
                        popUpTo(Screen.LandingScreen.route) { inclusive = true }
                    }
                    expanded = false
                })
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = warnaUMN)
    )
}