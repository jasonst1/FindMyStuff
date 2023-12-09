package com.anakbaikbaik.findmystuff.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.anakbaikbaik.findmystuff.Model.ItemMessage
import com.anakbaikbaik.findmystuff.NavBars.BottomNavBar
import com.anakbaikbaik.findmystuff.NavBars.TopBarWithLogout
import com.anakbaikbaik.findmystuff.Navigation.Screen
import com.anakbaikbaik.findmystuff.R
import com.anakbaikbaik.findmystuff.ViewModel.AuthViewModel
import com.anakbaikbaik.findmystuff.ViewModel.RoleViewModel
import com.anakbaikbaik.findmystuff.ui.theme.PrimaryTextButton
import com.anakbaikbaik.findmystuff.ui.theme.RedTextButton
import com.anakbaikbaik.findmystuff.ui.theme.warnaUMN
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(
    viewModel: AuthViewModel?,
    navController: NavController,
    firestoreViewModel: AuthViewModel?,
    roleViewModel: RoleViewModel?) {
    var itemMessages by remember {
        mutableStateOf<List<ItemMessage>>(emptyList())
    }

    var searchQuery by remember { mutableStateOf("") }

    // Initialize Firebase Firestore
    val db = Firebase.firestore

    // Reference to the "items" collection in Firestore
    val itemsCollectionRef = db.collection("items")

    // Read data from Firestore and populate the itemMessages list
    DisposableEffect(firestoreViewModel) {
        val listenerRegistration = itemsCollectionRef.addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                // Handle error here
                return@addSnapshotListener
            }

            // Convert Firestore documents to ItemMessage objects
            val messages = querySnapshot?.documents?.mapNotNull { document ->
                try {
                    val id = document.id
                    val nama = document.getString("nama") ?: ""
                    val lokasi = document.getString("lokasi") ?: ""
                    val deskripsi = document.getString("deskripsi") ?: ""
                    val status = document.getString("status") ?: ""
                    val gambar = document.getString("gambar") ?: ""
                    val pengambil = document.getString("pengambil") ?: ""
                    val gambarPengambil = document.getString("gambarPengambil") ?: ""
                    val nim = document.getString("nim") ?: ""
                    val tanggal = document.getString("tanggal") ?: ""

                    ItemMessage(id, nama, lokasi, deskripsi, status, gambar, pengambil, gambarPengambil, nim, tanggal)
                } catch (e: Exception) {
                    // Handle parsing error here
                    null
                }
            }

            // Update the itemMessages list with the retrieved data
            itemMessages = messages ?: emptyList()
        }

        onDispose {
            // Remove the Firestore listener when the composable is disposed
            listenerRegistration.remove()
        }
    }

    // Bikin filter yang ditampilin hanya yang mana?
    val filteredItemMessages = itemMessages.filter { it.status == "true" }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Log.d("HomeScreen", viewModel.toString())
        Scaffold(
            topBar = { TopBarWithLogout(viewModel, navController) },
            content = {it
                // Add padding to the main content area
                Conversation(viewModel, filteredItemMessages, navController, roleViewModel)
            },
            bottomBar = {
                BottomNavBar(viewModel, navController, roleViewModel)
            }
        )
    }
}

@Composable
fun Conversation(viewModel: AuthViewModel?, messages: List<ItemMessage>, navController: NavController, roleViewModel: RoleViewModel?) {
    val sortedMessages = messages.sortedByDescending { it.tanggal }

    var searchQuery by remember { mutableStateOf("") }

    val filteredMessages = if (searchQuery.isNotBlank()) {
        messages.filter {
            it.nama.contains(searchQuery, ignoreCase = true) ||
                    it.lokasi.contains(searchQuery, ignoreCase = true) ||
                    it.deskripsi.contains(searchQuery, ignoreCase = true)
        }
    } else {
        messages
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 56.dp) // Adjust the top padding as needed
    ) {
        Column {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            LazyColumn(
                modifier = Modifier.padding(top = 10.dp, bottom = 80.dp)
            ) {
                items(filteredMessages) { message ->

                    roleViewModel?.retrieveData()
                    val currentSession by roleViewModel!!.currentSession.collectAsState()

                    // Display data from the observed 'currentSession' in your UI
                    currentSession?.let { session ->
                        MessageCard(message, navController, session.role)
                    }
                }
            }
        }
    }
}

@Composable
fun MessageCard(itemMessage: ItemMessage, navController: NavController, userRole: String?) {
    val context = LocalContext.current
    Log.d("Image URL", itemMessage.gambar)

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp
            )
            .border(1.dp, Color.Black)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .background(color = warnaUMN)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = itemMessage.gambar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(400.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Row {
            Column(
                modifier = Modifier.padding(15.dp),
            ) {
                Text(
                    text = "Nama: ${itemMessage.nama}",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Black,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "ID : ${itemMessage.id}\n" +
                            "Tanggal : ${itemMessage.tanggal}\n" +
                            "Lokasi : ${itemMessage.lokasi}\n" +
                            "Deskripsi : ${itemMessage.deskripsi}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 15.sp
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            if (userRole == "1") {
                Column {
                    PrimaryTextButton(
                        text = stringResource(id = R.string.editButton),
                        onClick = {
                            navController.navigate("${Screen.EditScreen.route}/${itemMessage.id}")
                        }
                    )
                }
                Column {
                    RedTextButton(
                        text = stringResource(id = R.string.deleteButton)
                    ) {
                        navController.navigate("${Screen.DeleteScreen.route}/${itemMessage.id}")
                    }
                }
            }
        }
    }
}