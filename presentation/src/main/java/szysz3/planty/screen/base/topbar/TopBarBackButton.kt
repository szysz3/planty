package szysz3.planty.screen.base.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TopBarBackButton(
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = { }
) {
    // Reserve space for the back button even when it's not visible
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(48.dp), // Match the size of the IconButton
        contentAlignment = Alignment.Center
    ) {
        if (showBackButton) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
            }
        }
    }
}