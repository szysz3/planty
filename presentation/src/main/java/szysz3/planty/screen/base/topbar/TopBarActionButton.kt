package szysz3.planty.screen.base.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import szysz3.planty.theme.dimensions

@Composable
fun TopBarActionButton(
    icon: ImageVector = Icons.Rounded.Delete,
    showButton: Boolean = false,
    onAction: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(MaterialTheme.dimensions().size48),
        contentAlignment = Alignment.Center
    ) {
        if (showButton) {
            IconButton(onClick = onAction) {
                Icon(icon, contentDescription = null)
            }
        }
    }
}