@file:OptIn(ExperimentalMaterial3Api::class)

package com.anakbaikbaik.findmystuff.MainActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.anakbaikbaik.findmystuff.Navigation.Navigation
import com.anakbaikbaik.findmystuff.ViewModel.AuthViewModel
import com.anakbaikbaik.findmystuff.ViewModel.RoleViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<AuthViewModel>()
    private val roleViewModel by viewModels<RoleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        val db = FirebaseFirestore.getInstance()
        super.onCreate(savedInstanceState)
        setContent {
            Navigation(viewModel, roleViewModel)
        }
    }
}