package com.anakbaikbaik.findmystuff.NavBars

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
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import com.anakbaikbaik.findmystuff.Navigation.Screen
import com.anakbaikbaik.findmystuff.R
import com.anakbaikbaik.findmystuff.Screens.BottomNavigationItem
import com.anakbaikbaik.findmystuff.Screens.MessageCard
import com.anakbaikbaik.findmystuff.ViewModel.AuthViewModel
import com.anakbaikbaik.findmystuff.ViewModel.RoleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavBar(viewModel: AuthViewModel?, navController: NavController, roleViewModel: RoleViewModel?) {
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
            title = "ArchiveScreen",
            selectedIcon = Icons.Filled.Refresh,
            unselectedIcon = Icons.Outlined.Refresh,
            hasNews = false,
        )
    )
    val screenMap = mapOf(
        "HomeScreen" to Screen.HomeScreen,
        "AddScreen" to Screen.AddScreen,
        "ArchiveScreen" to Screen.ArchiveScreen
    )
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    roleViewModel?.retrieveData()
    val currentSession by roleViewModel!!.currentSession.collectAsState()
    var role = ""

    // Display data from the observed 'currentSession' in your UI
    currentSession?.let { session ->
      role = session.role
    }

    NavigationBar {
        NavigationBar(
            containerColor = Color.White
        ) {
            items.forEachIndexed{ index, item ->
                if (item.title == "AddScreen" && role != "1") {
                    // Skip this item if it's "AddScreen" and the user's role is not 1
                    return@forEachIndexed
                }
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
                            },
                        ) {
                            Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    item.unselectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = colorResource(R.color.white)
                    )
                )
            }
        }
    }
}