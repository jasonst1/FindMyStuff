package com.anakbaikbaik.findmystuff.Screens

import androidx.compose.material3.Icon
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.anakbaikbaik.findmystuff.R
import com.anakbaikbaik.findmystuff.ViewModel.AuthViewModel
import com.anakbaikbaik.findmystuff.ui.theme.PrimaryTextButton
import com.anakbaikbaik.findmystuff.ui.theme.RedTextButton
import com.anakbaikbaik.findmystuff.ui.theme.topBar
import com.anakbaikbaik.findmystuff.ui.theme.warnaUMN
import com.anakbaikbaik.findmystuff.Navigation.Screen

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(viewModel: AuthViewModel?, navController: NavController) {
    val items = listOf(
        BottomNavigationItem(
            title = "HomeScreen",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            hasNews = false,
        ),
        BottomNavigationItem(
            title = "AddScreen",
            selectedIcon = Icons.Filled.Add,
            unselectedIcon = Icons.Outlined.Add,
            hasNews = false,
        ),
        BottomNavigationItem(
            title = "EditScreen",
            selectedIcon = Icons.Filled.Refresh,
            unselectedIcon = Icons.Outlined.Refresh,
            hasNews = false,
        )
    )
    val screenMap = mapOf(
        "HomeScreen" to Screen.HomeScreen,
        "AddScreen" to Screen.AddScreen,
        "EditScreen" to Screen.EditScreen
    )
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Log.d("HomeScreen", viewModel.toString())
        Scaffold(
            topBar = { topBar() },
            content = {it
                // Add padding to the main content area
                Conversation(viewModel, SampleData.conversationSample, navController)
            },
            bottomBar = {
                NavigationBar() {
                    items.forEachIndexed{ index, item ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                                screenMap[item.title]?.let { navController.navigate(it.route) }
                            },
                            alwaysShowLabel = false,
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if(item.badgeCount != null) {
                                            Badge {
                                                Text(text = item.badgeCount.toString())
                                            }
                                        } else if(item.hasNews) {
                                            Badge()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (index == selectedItemIndex) {
                                            item.selectedIcon
                                        } else item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                }
                            }
                        )
                    }
                }
            }
        )
    }
}

object SampleData {
    // Extended sample conversation data
    val conversationSample = listOf(
        Message("Alice", "Hey, how's it going?"),
        Message("Bob", "I'm good, how about you?"),
        Message("Carol", "Hi everyone, what are you discussing?"),
        Message("David", "We're talking about the upcoming team outing. Any suggestions?"),
        Message("Alice", "How about a beach BBQ this Saturday?"),
        Message("Bob", "Sounds great! I'll bring the grill."),
        Message("Carol", "I can prepare some salads and bring drinks."),
        Message("David", "I'll handle the music and games."),
        Message("Emily", "Perfect, I'll send out the invites!"),
        Message("Frank", "Shall we bring anything else?"),
        Message("Alice", "Think of anything you'd like for a picnic. Let's make a list."),
        Message("Bob", "I'll bring some beach volleyballs and a frisbee."),
        Message("Carol", "Don't forget sunscreen and hats, everyone."),
        Message("David", "Looking forward to it! See you all on Saturday!"),
        // Add as many messages as needed to create a realistic conversation
    )
}

data class Message(val author: String, val body: String)

@Composable
fun Conversation(viewModel: AuthViewModel?, messages: List<Message>, navController: NavController) {
    LazyColumn (
        modifier = Modifier.padding(top = 64.dp)
    ) {
        items(messages) { message ->
            MessageCard(message, navController)
            Button(
                onClick = {
                    viewModel?.logout()
                    navController.navigate(Screen.LandingScreen.route) {
                        popUpTo(Screen.LandingScreen.route) { inclusive = true }
                    }
                }
            ){
                Text(text = "Logout")
            }
        }
    }
}

@Composable
fun MessageCard(message: Message, navController: NavController) {
    val context = LocalContext.current


    Column (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp
            )
            .border(1.dp, Color.Black)
    ) {
        Row (
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Column (
                modifier = Modifier
                    .background(color = warnaUMN)
            ) {
                Image(
                    painter = painterResource(R.drawable.monyet),
                    contentDescription = null,
                    modifier = Modifier
                        .size(400.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Row {
            Column (
                modifier = Modifier.padding(15.dp),
            ) {
                Text(
                    text = message.author,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Black,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = message.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 15.sp
                )
            }
        }
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column {
                PrimaryTextButton(
                    text = stringResource(id = R.string.editButton),
                    onClick = {
                        navController.navigate(Screen.EditScreen.route)
                    }
                )
            }
            Column {
                RedTextButton(
                    text = stringResource(id = R.string.deleteButton)
                ) {
                    // ERROR HANDLING FOR EMPTY INPUTFIELD.NAME
//                onButtonClick()
                }
            }
        }
    }
}