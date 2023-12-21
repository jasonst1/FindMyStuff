package com.anakbaikbaik.findmystuff.Util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import com.anakbaikbaik.findmystuff.Model.ImageData
import com.anakbaikbaik.findmystuff.Navigation.Screen
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun editToDb(nama : String, lokasi : String, deskripsi : String, image: ImageData, navController: NavController, itemId: String?){
    // Inisialisasi Firebase Firestore
    val storage = Firebase.storage
    val ref = storage.reference.child(System.currentTimeMillis().toString())
    var downloadUrl = ""

    var uri: Uri? = null
    var url: String? = null

    when (image) {
        is ImageData.UriData -> {
            // Use image.uri for Uri case
            uri = image.uri
        }
        is ImageData.StringData -> {
            // Use image.string for String case
            url = image.string
        }
    }

    if(nama.isNotEmpty() && lokasi.isNotEmpty() && deskripsi.isNotEmpty() && (uri != null || url != null)){
        if(uri != null && url == null){
            ref.putFile(uri).addOnSuccessListener { taskSnapshot ->
                ref.downloadUrl.addOnSuccessListener { uri ->
                    downloadUrl = uri.toString()
                    // Passing value ke function updateFirestore
                    updateFirestore(nama, lokasi, deskripsi, downloadUrl, navController, itemId)
                }
            }.addOnFailureListener { exception ->
                // Handle the error
                println("Image upload failed: $exception")
            }
        }
        else if(uri == null && url != null){
            navController.navigate(Screen.HomeScreen.route)
            updateFirestore(nama, lokasi, deskripsi, url, navController, itemId)
        }
        else{
            Log.d("EditScreen", "EditScreen: What The Hell")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun updateFirestore(nama : String, lokasi : String, deskripsi : String, image: String, navController: NavController, itemId: String?){
    val db = Firebase.firestore

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val current = LocalDateTime.now().format(formatter)

    // Assigning itemData to variable on database
    val itemData = hashMapOf(
        "nama" to nama,
        "lokasi" to lokasi,
        "deskripsi" to deskripsi,
        "status" to "true",
        "gambar" to image,
        "tanggal" to current
    )

    // Store the data to database
    db.collection("items").document(itemId!!).set(itemData).addOnSuccessListener {
        navController.navigate(Screen.HomeScreen.route)
    }.addOnFailureListener {
        Log.d("EditScreen", "EditScreen: Failed to update Firestore")
    }
}

suspend fun retrieveData(itemId: String?): DocumentSnapshot? {
    val db = Firebase.firestore
    val docRef = db.collection("items").document(itemId.toString()).get().await()

    return docRef
}

@RequiresApi(Build.VERSION_CODES.O)
private var isUploadInProgress = false

@RequiresApi(Build.VERSION_CODES.O)
fun uploadToDb(nama : String, lokasi : String, deskripsi : String, imageUri : Uri?, navController: NavController){
    if (nama.isNotEmpty() && lokasi.isNotEmpty() && deskripsi.isNotEmpty() && imageUri != null) {
        // Inisialisasi Firebase Firestore
        val db = Firebase.firestore
        val storage = Firebase.storage
        val ref = storage.reference.child(System.currentTimeMillis().toString())
        var downloadUrl = ""

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val current = LocalDateTime.now().format(formatter)

        if (isUploadInProgress || nama.isEmpty() || lokasi.isEmpty() || deskripsi.isEmpty() || imageUri == null) {
            // Upload is already in progress or missing required data
            return
        }

        // Set the flag to indicate that an upload is in progress
        isUploadInProgress = true

        GlobalScope.launch(Dispatchers.IO) {
            try {
                imageUri?.let {
                    ref.putFile(it).addOnSuccessListener {taskSnapshot->
                        ref.downloadUrl.addOnSuccessListener { uri->
                            downloadUrl = uri.toString()
                            // Membuat objek data
                            // Assigning value that passed from addScreen to database
                            val itemData = hashMapOf(
                                "nama" to nama,
                                "lokasi" to lokasi,
                                "deskripsi" to deskripsi,
                                "status" to "true",
                                "gambar" to downloadUrl,
                                "tanggal" to current
                            )

                            // Menambahkan data ke koleksi "items" in database
                            db.collection("items")
                                .add(itemData)
                                .addOnSuccessListener {
                                    // Handle sukses (opsional)
                                    navController.navigate(Screen.HomeScreen.route)
                                }
                                .addOnFailureListener {
                                }
                        }
                    }
                }
            } catch (e: Exception) {
                // Handle failure
            } finally {
                // Reset the flag once the upload is completed or failed
                isUploadInProgress = false
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