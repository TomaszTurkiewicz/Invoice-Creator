package com.tt.invoicecreator.helpers

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.tt.invoicecreator.R

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun rememberGoogleSignInLauncher(
    onSignInSuccess: () -> Unit,
    onSignInError: (String) -> Unit
): () -> Unit{
    val context = LocalContext.current
    val auth = Firebase.auth

    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    val googleSignInClient: GoogleSignInClient = remember {
        GoogleSignIn.getClient(context,gso)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential =
                    com.google.firebase.auth.GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            onSignInSuccess()
                        } else {
                            onSignInError(authTask.exception?.message ?: "Firebase auth failed")
                        }
                    }
            } catch (e: ApiException) {
                onSignInError("Google Sign-In failed: ${e.localizedMessage}")
            }
        } else {
            onSignInError("Google Sign-In canceled")

        }
    }
    return {
        launcher.launch(googleSignInClient.signInIntent)
    }
}
