package com.anakbaikbaik.findmystuff.ViewModel

import androidx.lifecycle.ViewModel
import com.anakbaikbaik.findmystuff.Model.ItemMessage
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FirestoreViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _itemMessages = MutableStateFlow<List<ItemMessage>>(emptyList())
    val itemMessages: StateFlow<List<ItemMessage>> = _itemMessages.asStateFlow()

    init {
        // Fetch data from Firestore and update _itemMessages
        fetchDataFromFirestore()
    }

    private fun fetchDataFromFirestore() {
        firestore.collection("items")
            .get()
            .addOnSuccessListener { result ->
                val itemMessagesList = mutableListOf<ItemMessage>()
                for (document in result) {
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

                    itemMessagesList.add(ItemMessage(id, nama, lokasi, deskripsi, status, gambar, pengambil, gambarPengambil, nim, tanggal))
                }
                _itemMessages.value = itemMessagesList
            }
            .addOnFailureListener { exception ->
                // Handle the error here
            }
    }
}