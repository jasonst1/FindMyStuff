package com.anakbaikbaik.findmystuff.Screens

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.anakbaikbaik.findmystuff.Model.ItemMessage
import com.anakbaikbaik.findmystuff.NavBars.BottomNavBar
import com.anakbaikbaik.findmystuff.NavBars.TopBarWithLogout
import com.anakbaikbaik.findmystuff.ViewModel.AuthViewModel
import com.anakbaikbaik.findmystuff.ViewModel.RoleViewModel
import com.anakbaikbaik.findmystuff.ui.theme.warnaUMN
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ArchiveScreen(viewModel: AuthViewModel?, navController: NavController, firestoreViewModel: AuthViewModel?, roleViewModel: RoleViewModel?) {
    var itemMessages by remember {
        mutableStateOf<List<ItemMessage>>(emptyList())
    }

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
                    val fotoPengambil = document.getString("fotoPengambil") ?: ""
                    val nim = document.getString("nim") ?: ""
                    val tanggal = document.getString("tanggal") ?: ""

                    ItemMessage(id, nama, lokasi, deskripsi, status, gambar, pengambil, fotoPengambil, nim, tanggal)
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
    val filteredItemMessages = itemMessages.filter { it.status == "false" }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Log.d("ArchiveScreen", viewModel.toString())
        Scaffold(
            topBar = { TopBarWithLogout(viewModel, navController) },
            content = {it
                // Add padding to the main content area
                calling(viewModel, filteredItemMessages, navController)
            },
            bottomBar = {
                BottomNavBar(viewModel, navController, roleViewModel)
            }
        )
    }
}

@Composable
fun calling(viewModel: AuthViewModel?, messages: List<ItemMessage>, navController: NavController) {
    LazyColumn (
        modifier = Modifier.padding(top = 64.dp, bottom = 80.dp)
    ) {
        items(messages) { message ->
            MessageCardArchive(message, navController)
            // Display user information
//            viewModel?.currentUser?.let { user ->
//                Text("Username: ${user.displayName ?: "N/A"}")
//                Text("Email: ${user.email ?: "N/A"}")
//            }
//            Button(
//                onClick = {
//                    viewModel?.logout()
//                    navController.navigate(Screen.LandingScreen.route) {
//                        popUpTo(Screen.LandingScreen.route) { inclusive = true }
//                    }
//                }
//            ){
//                Text(text = "Logout")
//            }
        }
    }
}

@Composable
fun MessageCardArchive(itemMessage: ItemMessage, navController: NavController) {
    val context = LocalContext.current
    Log.d("Image URL", itemMessage.gambar)

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp
            )
            .border(1.dp, Color.Black),
        verticalArrangement = Arrangement.Center
    ) {
        Row (
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Column {
                Row (
                    modifier = Modifier.padding(5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column (
                        modifier = Modifier
                            .background(color = warnaUMN)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = itemMessage.gambar),
                            contentDescription = null,
                            modifier = Modifier
                                .size(180.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            Column {
                Row (
                    modifier = Modifier.padding(5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column (
                        modifier = Modifier
                            .background(color = warnaUMN)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = itemMessage.fotoPengambil),
                            contentDescription = null,
                            modifier = Modifier
                                .size(180.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
        Row {
            Column (
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
            ) {
                Text(
                    text = "Nama: ${itemMessage.nama}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Kode : ${itemMessage.id}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Pengambil : ${itemMessage.pengambil}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "NIM : ${itemMessage.nim}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 15.sp
                )
            }
        }
    }
}