package com.anakbaikbaik.findmystuff.Screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.painter.ColorPainter
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
import com.anakbaikbaik.findmystuff.Model.ImageData
import com.anakbaikbaik.findmystuff.NavBars.BottomNavBar
import com.anakbaikbaik.findmystuff.NavBars.TopBarWithLogout
import com.anakbaikbaik.findmystuff.Navigation.Screen
import com.anakbaikbaik.findmystuff.R
import com.anakbaikbaik.findmystuff.Util.editToDb
import com.anakbaikbaik.findmystuff.Util.getCapturedImageUri
import com.anakbaikbaik.findmystuff.Util.retrieveData
import com.anakbaikbaik.findmystuff.ViewModel.AuthViewModel
import com.anakbaikbaik.findmystuff.ViewModel.RoleViewModel
import com.anakbaikbaik.findmystuff.ui.theme.GreenTextButton
import com.anakbaikbaik.findmystuff.ui.theme.RedTextButton
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.google.firebase.firestore.DocumentSnapshot


@RequiresApi(Build.VERSION_CODES.O)
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
                    // Passing itemId from Home to EditArea for execution
                    EditArea(navController, itemId)
                },
                bottomBar = {
                    BottomNavBar(viewModel, navController, roleViewModel)
                }
            )
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EditArea(navController: NavController, itemId: String?) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 80.dp, bottom = 100.dp)
            // For make the screen scrollable
            .verticalScroll(state = scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Inisialisasi Variable yang akan di input

        var docRef by remember { mutableStateOf<DocumentSnapshot?>(null) }

        var nama by remember { mutableStateOf("") }
        var lokasi by remember { mutableStateOf("") }
        var deskripsi by remember { mutableStateOf("") }
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        var initialImage by remember { mutableStateOf<String?>(null) }
        var isPhotoChanged by remember { mutableStateOf(false) }

        val context = LocalContext.current

        var imageBitmap by remember{ mutableStateOf<Bitmap?>(null)}

        LaunchedEffect(itemId) {
            docRef = retrieveData(itemId)

            // Calling the data from id yang diminta
            docRef?.let{ documentSnapshot ->
                nama = documentSnapshot?.get("nama").toString()
                lokasi = documentSnapshot?.get("lokasi").toString()
                deskripsi = documentSnapshot?.get("deskripsi").toString()
                initialImage = documentSnapshot?.get("gambar").toString()
            }
        }

        // Camera Launcher
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

        // Camera Permission
        val cameraPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, launch camera
                imageUri = getCapturedImageUri(context)
                cameraLauncher.launch(imageUri)
            } else {
                // Permission denied, handle accordingly
                println("Camera permission denied")
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Edit Barang",
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // For showing the image of edited content
        imageBitmap?.let { imageBitmap ->
            initialImage = null
            Image(
                painter = rememberAsyncImagePainter(imageBitmap),
                contentDescription = null,
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop)
        }

        // For showing the initImage that got from content id
        initialImage?.let { initialImage ->
            GlideImage(
                model = initialImage,
                loading = placeholder(ColorPainter(Color.Red)),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Gallery launcher
        val galleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { result: Uri? ->
                imageUri = result
                imageBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
                isPhotoChanged = true
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
                    val imageData: ImageData = if (isPhotoChanged) {
                        ImageData.UriData(imageUri!!)
                    } else {
                        ImageData.StringData(initialImage!!)
                    }
                    // Store data that have been edited
                    editToDb(nama, lokasi, deskripsi, imageData, navController, itemId)
                }
            }
        }
    }
}