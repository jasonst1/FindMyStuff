package com.anakbaikbaik.findmystuff.Screens

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.anakbaikbaik.findmystuff.Navigation.Screen
import com.anakbaikbaik.findmystuff.R
import com.anakbaikbaik.findmystuff.ViewModel.AuthViewModel
import com.anakbaikbaik.findmystuff.ui.theme.GreenTextButton
import com.anakbaikbaik.findmystuff.ui.theme.RedTextButton
import com.anakbaikbaik.findmystuff.ui.theme.TopBarWithLogout
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddScreen(viewModel: AuthViewModel?, navController: NavController) {
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
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        content = {
            Scaffold(
                topBar = { TopBarWithLogout(viewModel, navController) },
                content = {it
                    AddArea(navController)
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
                                        },
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
    )
}

@Composable
fun AddArea(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var nama by remember { mutableStateOf("") }
        var lokasi by remember { mutableStateOf("") }
        var deskripsi by remember { mutableStateOf("") }
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        val context = LocalContext.current

        val cameraPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, launch camera
                launchCamera(context)
            } else {
                // Permission denied, handle accordingly
                println("Camera permission denied")
            }
        }

        val cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean> = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { isSuccessful: Boolean ->
            if (isSuccessful) {
                // Handle successful capture
                // You can perform additional actions if needed
            } else {
                println("Image capture canceled or unsuccessful")
            }
        }

        imageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(model = uri),
                contentDescription = null,
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop
            )
        }

        val galleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { result: Uri? ->
                imageUri = result
            }
        )
        OutlinedTextField(
            value = nama,
            onValueChange = { value -> nama = value },
            label = { Text("Name") }
        )
        OutlinedTextField(
            value = lokasi,
            onValueChange = { value -> lokasi = value },
            label = { Text("Lokasi") }
        )
        OutlinedTextField(
            value = deskripsi,
            onValueChange = { value -> deskripsi = value },
            label = { Text("Deskripsi") }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            FloatingActionButton(
                onClick = {
                    if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                        val hasPermission = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED

                        if (hasPermission) {
                            // Permission granted, launch camera
                            cameraLauncher.launch(getCapturedImageUri(context))
                        } else {
                            // Request camera permission
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    } else {
                        // Device does not have a camera, handle accordingly
                        println("Device does not have a camera")
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = null
                )
            }
            FloatingActionButton(
                        onClick = {
                            // Launch the gallery intent to select an image
                            galleryLauncher.launch("image/*")
                        },
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.folder),
                            contentDescription = null
                        )
                    }
                }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column {
                RedTextButton(
                    text = stringResource(id = R.string.cancelButton),
                    onClick = {
                        navController.navigate(Screen.HomeScreen.route)
                    }
                )
            }
            Column {
                GreenTextButton(
                    text = stringResource(id = R.string.approveButton)
                ) {
                    // ERROR HANDLING FOR EMPTY INPUTFIELD.NAME
                    if (nama.isNotEmpty() && lokasi.isNotEmpty() && deskripsi.isNotEmpty() && imageUri != null) {
                        // Inisialisasi Firebase Firestore
                        val db = Firebase.firestore

                        // Membuat objek data
                        val itemData = hashMapOf(
                            "nama" to nama,
                            "lokasi" to lokasi,
                            "deskripsi" to deskripsi,
                            "status" to "true",
                            "gambar" to imageUri.toString(),
                        )

                        // Menambahkan data ke koleksi "items"
                        db.collection("items")
                            .add(itemData)
                            .addOnSuccessListener {
                                // Handle sukses (opsional)
                                navController.navigate(Screen.HomeScreen.route)
                            }
                            .addOnFailureListener {
                                // Handle gagal (opsional)
                                // Anda dapat menampilkan pesan kesalahan atau mencatat pesan kesalahan
                            }
                    }
                }
            }
        }
    }
}

fun getCapturedImageUri(context: Context): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
    }

    return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
}

fun launchCamera(context: Context) {
    try {
        // Start the camera activity using the photoUri
    } catch (e: Exception) {
        println("Error launching camera: ${e.message}")
    }
}