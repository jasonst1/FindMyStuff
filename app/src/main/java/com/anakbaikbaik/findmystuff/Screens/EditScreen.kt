package com.anakbaikbaik.findmystuff.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.anakbaikbaik.findmystuff.R
import com.anakbaikbaik.findmystuff.ui.theme.GreenTextButton
import com.anakbaikbaik.findmystuff.ui.theme.RedTextButton
import com.anakbaikbaik.findmystuff.ui.theme.topBar


@Composable
fun EditScreen(navController: NavController){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = { topBar() },
            content = {it
                // Add padding to the main content area
                EditArea(navController)
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
            Column {
                GreenTextButton(
                    text = stringResource(id = R.string.approveButton)
                ) {
                    // ERROR HANDLING FOR EMPTY INPUTFIELD.NAME
//                onButtonClick()
                }
            }
        }
    }
}