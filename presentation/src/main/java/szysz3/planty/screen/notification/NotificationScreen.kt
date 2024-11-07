package szysz3.planty.screen.notification

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun NotificationsScreen() {
    Text(
        text = "Notification screen",
        modifier = Modifier.fillMaxSize(),
        textAlign = TextAlign.Center
    )
}