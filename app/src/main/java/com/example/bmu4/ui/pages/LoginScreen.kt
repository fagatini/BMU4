package com.example.bmu4.ui.pages

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.bmu4.auth.GoogleAuthManager
import com.example.bmu4.storage.SecureStorage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginSuccess: (String, String?) -> Unit) {
    val context = LocalContext.current
    var error by remember { mutableStateOf<String?>(null) }
    val activity = context as FragmentActivity
    val executor = ContextCompat.getMainExecutor(context)

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data: Intent? = result.data
        if (data != null) {
            val task = com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(data)
            GoogleAuthManager.handleSignInResult(
                completedTask = task,
                onSuccess = { account ->
                    val token = account.idToken
                    if (token != null) {
                        onLoginSuccess(token, account.displayName)
                    } else {
                        error = "Token is null"
                    }
                },
                onError = { message ->
                    error = message
                }
            )
        } else {
            error = "Canceled or failed to authenticate"
        }
    }

    fun showBiometricPrompt() {
        val prompt = BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                var name: String? = null
                SecureStorage(context).getName()?.let {
                    name = it
                } ?: run {
                    error = "There is no saved token"
                }

                SecureStorage(context).getToken()?.let {
                    onLoginSuccess(it, name)
                } ?: run {
                    error = "There is no saved token"
                }
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                error = "Error: $errString"
            }
        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Биометрическая аутентификация")
            .setSubtitle("Вход с помощью отпечатка пальца")
            .setNegativeButtonText("Отмена")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        prompt.authenticate(promptInfo)
    }


    Scaffold(
        topBar = { TopAppBar(title = { Text("Login", modifier = Modifier.fillMaxWidth()) }) }) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Button(onClick = {
                val intent = GoogleAuthManager.getSignInIntent(context)
                launcher.launch(intent)
            }) {
                Text("Login with Google")
            }

            Button(onClick = { showBiometricPrompt() }) {
                Text("Login using biometrics")
            }

            error?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Error: $it", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
