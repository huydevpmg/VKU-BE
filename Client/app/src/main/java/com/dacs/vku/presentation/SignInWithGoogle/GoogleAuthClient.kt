package com.dacs.vku.presentation.SignInWithGoogle

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.dacs.vku.R
import com.dacs.vku.domain.model.SignInResult
import com.dacs.vku.domain.model.UserData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthClient(
    val context: Context,
    val oneTapClient: SignInClient


) {
    private val auth = Firebase.auth

    suspend fun  signIn():IntentSender?{
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }
    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdtoken = credential.googleIdToken
        val googleCredential = GoogleAuthProvider.getCredential(googleIdtoken, null)
        return try {
            val user = auth.signInWithCredential(googleCredential).await().user
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName.toString(),
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null

            )

        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }

    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
    fun getSignInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName.toString(),
            profilePictureUrl = photoUrl?.toString()
        )
    }

    suspend fun signOut() {
        try {
        oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }
}