package com.example.bmu4.ui.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.bmu4.auth.GoogleAuthManager

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(name:String?, onLogout: () -> Unit) {
    val context = LocalContext.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("Home") }) },
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("You are logged in! $name")
            Button(onClick = { GoogleAuthManager.signOut(context, onLogout); }) {
                Text("Logout")
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}