package szysz3.planty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import dagger.hilt.android.AndroidEntryPoint
import szysz3.planty.screen.main.screen.MainScreen
import szysz3.planty.ui.theme.PlantyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlantyTheme(dynamicColor = false) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.dark(MaterialTheme.colorScheme.background.toArgb()),
                    navigationBarStyle = SystemBarStyle.dark(MaterialTheme.colorScheme.background.toArgb())
                )
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    MainScreen()
}