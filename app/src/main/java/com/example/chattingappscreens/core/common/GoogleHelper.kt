package com.example.chattingappscreens.core.common

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.example.chattingappscreens.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL

object GoogleHelper {


    suspend fun getIdToken(context: Context): String? {
        try {

            val getGoogleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(context.getString(R.string.web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build()

            val getCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(getGoogleIdOption)
                .build()

            val getCredentialResponse = CredentialManager.create(context)
                .getCredential(context, request = getCredentialRequest)

            return handleCredential(response = getCredentialResponse)

        } catch (e: Exception) {
            e.printStackTrace()
          return null
        }
    }

    private fun handleCredential(response: GetCredentialResponse): String? {


        val credential = response.credential
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            return googleIdTokenCredential.idToken
        } else {
           return null
        }
    }
}



//    private fun getByIntent() {
//
//    }
//
//    fun getIntent(): Intent{
//        return Intent(Settings.ACTION_ADD_ACCOUNT).apply {
//            putExtra(Settings.EXTRA_ACCOUNT_TYPES , arrayOf("com.google"))
//        }
//    }

