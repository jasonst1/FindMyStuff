package com.anakbaikbaik.findmystuff.Screens

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.anakbaikbaik.findmystuff.NavBars.BottomNavBar
import com.anakbaikbaik.findmystuff.NavBars.TopBarWithLogout
import com.anakbaikbaik.findmystuff.Navigation.Screen
import com.anakbaikbaik.findmystuff.R
import com.anakbaikbaik.findmystuff.ViewModel.AuthViewModel
import com.anakbaikbaik.findmystuff.ViewModel.RoleViewModel
import com.anakbaikbaik.findmystuff.ui.theme.GreenTextButton
import com.anakbaikbaik.findmystuff.ui.theme.RedTextButton
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EditScreen(viewModel: AuthViewModel?, itemId: String?, navController: NavController, roleViewModel: RoleViewModel?) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        content = {
            Scaffold(
                topBar = { TopBarWithLogout(viewModel, navController) },
                content = {it
                    EditArea(navController, itemId)
                },
                bottomBar = {
                    BottomNavBar(viewModel, navController, roleViewModel)
                }
            )
        }
    )
}

@Composable
fun EditArea(navController: NavController, itemId: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var docRef by remember { mutableStateOf<DocumentSnapshot?>(null) }

        LaunchedEffect(itemId) {
            docRef = retrieveData(itemId)
        }

        var nama by remember { mutableStateOf("") }
        var lokasi by remember { mutableStateOf("") }
        var deskripsi by remember { mutableStateOf("") }
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        var isPhotoChanged by remember { mutableStateOf(false) }

        val context = LocalContext.current

        var imageBitmap by remember{ mutableStateOf<Bitmap?>(null)}

        docRef?.let{ documentSnapshot ->
            nama = documentSnapshot?.get("nama").toString()
            lokasi = documentSnapshot?.get("lokasi").toString()
            deskripsi = documentSnapshot?.get("deskripsi").toString()
            imageUri = Uri.parse(documentSnapshot?.get("gambar").toString())
        }

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
                imageBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
                isPhotoChanged = true
            } else {
                println("Image capture canceled or unsuccessful")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Edit Barang",
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
//                color = warnaUMN
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        imageBitmap?.let { imageBitmap ->
            Image(
                painter = rememberAsyncImagePainter(imageBitmap),
                contentDescription = null,
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        val galleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { result: Uri? ->
                imageUri = result
                imageBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            FloatingActionButton(
                containerColor = Color.White,
                onClick = {
                    if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                        val hasPermission = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED

                        if (hasPermission) {
                            // Permission granted, launch camera
                            imageUri = getCapturedImageUri(context)
                            cameraLauncher.launch(imageUri)
                        } else {
                            // Request camera permission
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    } else {
                        // Device does not have a camera, handle accordingly
                        println("Device does not have a camera")
                    }
                },
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = null
                )
            }
            FloatingActionButton(
                containerColor = Color.White,
                onClick = {
                    // Launch the gallery intent to select an image
                    galleryLauncher.launch("image/*")
                },
                modifier = Modifier
                    .padding(start = 16.dp, top = 10.dp, bottom = 16.dp)
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
                    editToDb(nama, lokasi, deskripsi, imageUri, navController, itemId)
                }
            }
        }
    }
}

suspend fun retrieveData(itemId: String?): DocumentSnapshot? {
    val db = Firebase.firestore
    val docRef = db.collection("items").document(itemId.toString()).get().await()

    return docRef
}

fun editToDb(nama : String, lokasi : String, deskripsi : String, imageUri : Uri?, navController: NavController, itemId: String?){
    if (nama.isNotEmpty() && lokasi.isNotEmpty() && deskripsi.isNotEmpty() && imageUri != null) {
        // Inisialisasi Firebase Firestore
        val db = Firebase.firestore
        val storage = Firebase.storage
        val ref = storage.reference.child(System.currentTimeMillis().toString())
        var downloadUrl = ""

        imageUri?.let {
            ref.putFile(it).addOnSuccessListener {taskSnapshot->
                ref.downloadUrl.addOnSuccessListener { uri->
                    downloadUrl = uri.toString()
                    // Membuat objek data
                    val itemData = hashMapOf(
                        "nama" to nama,
                        "lokasi" to lokasi,
                        "deskripsi" to deskripsi,
                        "status" to "true",
                        "gambar" to downloadUrl
                    )

                    // Menambahkan data ke koleksi "items"
                    if (itemId != null) {
                        db.collection("items")
                            .document(itemId)
                            .set(itemData, SetOptions.merge())
                            .addOnSuccessListener {
                                // Handle sukses (opsional)
                                navController.navigate(Screen.HomeScreen.route)
                            }
                            .addOnFailureListener {
                            }
                    }
                }
            }
        }
    }
}

fun capturedImageUri(context: Context): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
    }

    return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
}

fun camera(context: Context) {
    try {
        // Start the camera activity using the photoUri
    } catch (e: Exception) {
        println("Error launching camera: ${e.message}")
    }
}