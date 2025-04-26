package com.example.bmu4

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.example.bmu4.storage.SecureStorage
import com.example.bmu4.ui.pages.HomeScreen
import com.example.bmu4.ui.pages.LoginScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface {
                    val token = remember { mutableStateOf<String?>(null) }
                    val name = remember { mutableStateOf<String?>(null) }

                    if (token.value != null) {
                        HomeScreen(
                            name.value,
                            onLogout = {
                                token.value = null
                            }
                        )
                    } else {
                        LoginScreen(
                            onLoginSuccess = { newToken, accountName ->
                                SecureStorage(applicationContext).saveToken(newToken)

                                if (accountName != null) {
                                    SecureStorage(applicationContext).saveName(accountName)
                                }

                                token.value = newToken
                                name.value = accountName
                            }
                        )
                    }
                }
            }
        }
    }
}
