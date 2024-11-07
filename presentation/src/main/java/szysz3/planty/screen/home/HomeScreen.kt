package szysz3.planty.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun HomeScreen() {
    Text(
        text = "Home screen",
        modifier = Modifier.fillMaxSize(),
        textAlign = TextAlign.Center
    )
}