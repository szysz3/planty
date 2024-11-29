package szysz3.planty.screen.login.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import szysz3.planty.R
import szysz3.planty.domain.model.AuthResult
import szysz3.planty.screen.login.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onSuccessfulLogin: @Composable () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val intent = result.data
            if (intent != null) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { idToken ->
                    viewModel.loginWithGoogle(idToken)
                }
            }
        }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            when (uiState) {
                is AuthResult.Loading -> CircularProgressIndicator()
                is AuthResult.Success -> {
                    val user = (uiState as AuthResult.Success).userId
                    if (user != null) {
                        onSuccessfulLogin()
                    } else {
                        Button(onClick = { launcher.launch(googleSignInClient.signInIntent) }) {
                            Text("Sign In with Google")
                        }
                    }
                }

                is AuthResult.Idle -> {
                    Button(onClick = { launcher.launch(googleSignInClient.signInIntent) }) {
                        Text("Sign In with Google")
                    }
                }

                is AuthResult.Error -> {
                    Text(
                        "Error: ${(uiState as AuthResult.Error).message}",
                        color = MaterialTheme.colors.error
                    )
                    Button(onClick = { launcher.launch(googleSignInClient.signInIntent) }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}
