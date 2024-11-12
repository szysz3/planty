package szysz3.planty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import szysz3.planty.screen.main.screen.MainScreen
import szysz3.planty.ui.theme.PlantyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlantyTheme(dynamicColor = false) {
                MainScreen()
            }
        }
    }
}
