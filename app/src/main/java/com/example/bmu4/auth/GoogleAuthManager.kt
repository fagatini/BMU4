package com.example.bmu4.auth

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

object GoogleAuthManager {
    private const val WEB_CLIENT_ID = "918619930046-s92h6m97vo3rpn400uit46561eabloj5.apps.googleusercontent.com"

    private fun getSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(WEB_CLIENT_ID)
            .build()
    }

    fun getSignInIntent(context: Context): Intent {
        val client = GoogleSignIn.getClient(context, getSignInOptions())
        return client.signInIntent
    }

    fun handleSignInResult(
        completedTask: Task<GoogleSignInAccount>,
        onSuccess: (GoogleSignInAccount) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            onSuccess(account)
        } catch (e: ApiException) {
            onError("Sign in failed: ${e.statusCode} ${e.localizedMessage}")
        }
    }

    fun signOut(context: Context, onComplete: () -> Unit) {
        GoogleSignIn.getClient(context, getSignInOptions()).signOut()
            .addOnCompleteListener { onComplete() }
    }
}
