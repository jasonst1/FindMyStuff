package com.anakbaikbaik.findmystuff.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
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

//UI Element for displaying a title
@Composable
fun OnBackgroundTitleText(text: String, modifier: Modifier) {
    TitleText(text = text, color =
    MaterialTheme.colorScheme.onBackground)
}
//Here, we use the titleLarge style from the typography
@Composable
fun TitleText(text: String, color: Color) {
    Text(text = text, style =
    MaterialTheme.typography.titleLarge, color = color)
}
//UI Element for displaying an item list
@Composable
fun OnBackgroundItemText(text: String) {
    ItemText(text = text, color =
    MaterialTheme.colorScheme.onBackground)
}
//Here, we use the bodySmall style from the typography
@Composable
fun ItemText(text: String, color: Color) {
    Text(text = text, style =
    MaterialTheme.typography.bodySmall, color = color)
}
//UI Element for displaying a button
@Composable
fun PrimaryTextButton(text: String, onClick: () -> Unit) {
    editButton(
        text = text,
        textColor = Color.White,
        onClick = onClick
    )
}

@Composable
fun GreenTextButton(text: String, onClick: () -> Unit) {
    acceptButton(
        text = text,
        textColor = Color.White,
        onClick = onClick
    )
}

@Composable
fun RedTextButton(text: String, onClick: () -> Unit) {
    delButton(
        text = text,
        textColor = Color.White,
        onClick = onClick
    )
}
//Here, we use the labelMedium style from the typography
@Composable
fun editButton(text: String, textColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .width(100.dp),
        colors = ButtonDefaults
            .buttonColors(
                containerColor = Color.Black,
                contentColor = textColor
            )
    ) {
        Text(text = text, style =
        MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun delButton(text: String, textColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .width(100.dp),
        colors = ButtonDefaults
            .buttonColors(
                containerColor = Merah,
                contentColor = textColor
            )
    ) {
        Text(text = text, style =
        MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun acceptButton(text: String, textColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .width(100.dp),
        colors = ButtonDefaults
            .buttonColors(
                containerColor = Hijau,
                contentColor = textColor
            )
    ) {
        Text(text = text, style =
        MaterialTheme.typography.labelMedium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBar() {
    TopAppBar(
        title = {
            Text(
                stringResource(id = R.string.app_name),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = White,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = warnaUMN)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithLogo(viewModel: AuthViewModel?, navController: NavController) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = warnaUMN)
            .padding(5.dp),
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(
                onClick = {
                    viewModel?.logout()
                    navController.navigate(Screen.LandingScreen.route) {
                        popUpTo(Screen.LandingScreen.route) { inclusive = true }
                    }
                }
            ) {
                Text(text = "Logout")
            }
        }
    )
}

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