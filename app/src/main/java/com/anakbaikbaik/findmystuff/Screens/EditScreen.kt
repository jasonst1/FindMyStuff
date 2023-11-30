package com.anakbaikbaik.findmystuff.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.anakbaikbaik.findmystuff.Navigation.Screen
import com.anakbaikbaik.findmystuff.R
import com.anakbaikbaik.findmystuff.ui.theme.GreenTextButton
import com.anakbaikbaik.findmystuff.ui.theme.RedTextButton
import com.anakbaikbaik.findmystuff.ui.theme.topBar

data class Item(
//    val id: String,
    val nama: String,
    val lokasi: String,
    val deskripsi: String
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EditScreen(navController: NavController){
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
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Scaffold(
            topBar = { topBar() },
            content = {it
                // Add padding to the main content area
                EditArea(navController)
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White
                ) {
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
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = colorResource(R.color.white)
                            )
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun EditArea(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var nama by remember { mutableStateOf("") }
        var lokasi by remember { mutableStateOf("") }
        var deskripsi by remember { mutableStateOf("") }

        Image(
            painter = painterResource(R.drawable.monyet),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "Name"
        )
        OutlinedTextField(
            value = nama,
            onValueChange = { nama = it },
            label = { Text("Label") }
        )
        Text(
            text = "Lokasi"
        )
        OutlinedTextField(
            value = lokasi,
            onValueChange = { lokasi = it },
            label = { Text("Label") }
        )
        Text(
            text = "Deskripsi"
        )
        OutlinedTextField(
            value = deskripsi,
            onValueChange = { deskripsi = it },
            label = { Text("Label") }
        )
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column {
                RedTextButton(
                    text = stringResource(id = R.string.cancelButton),
                ) {
                    // ERROR HANDLING FOR EMPTY INPUTFIELD.NAME
//                onButtonClick()
                }
            }
            // In your ViewModel or repository, add a function to update the item
//            fun updateItem(item: Item) {
//                // Get a reference to the Firestore collection where your items are stored
//                val itemsCollectionRef = Firebase.firestore.collection("items")
//
//                // Update the item in Firestore
//                itemsCollectionRef.document(item.id).update(
//                    mapOf(
//                        "nama" to item.nama,
//                        "lokasi" to item.lokasi,
//                        "deskripsi" to item.deskripsi
//                    )
//                ).addOnSuccessListener {
//                    // Handle success, e.g., show a toast or navigate back to the previous screen
//                }.addOnFailureListener { e ->
//                    // Handle failure, e.g., show an error message
//                }
//            }
            Column {
                GreenTextButton(
                    text = stringResource(id = R.string.approveButton)
                ) {
//                    val updatedItem = Item(
//                        id = "item_id", // Replace with the actual item ID
//                        nama = nama,
//                        lokasi = lokasi,
//                        deskripsi = deskripsi
//                    )
//                    updateItem(updatedItem)
                }
            }
        }
    }
}