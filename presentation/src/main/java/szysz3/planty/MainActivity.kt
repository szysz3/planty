package szysz3.planty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import szysz3.planty.domain.model.AuthState
import szysz3.planty.screen.login.screen.LoginScreen
import szysz3.planty.screen.login.viewmodel.LoginViewModel
import szysz3.planty.screen.main.screen.MainScreen
import szysz3.planty.ui.theme.PlantyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlantyTheme(dynamicColor = false) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.dark(MaterialTheme.colorScheme.surface.toArgb()),
                    navigationBarStyle = SystemBarStyle.dark(MaterialTheme.colorScheme.surface.toArgb())
                )
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent(loginViewModel: LoginViewModel = hiltViewModel()) {
    val authState by loginViewModel.authState.collectAsState()

    if (authState == AuthState.LOGGED_IN) {
        MainScreen()
    } else {
        LoginScreen {
            MainScreen()
        }
    }
}