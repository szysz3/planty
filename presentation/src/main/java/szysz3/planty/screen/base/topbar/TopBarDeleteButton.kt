package szysz3.planty.screen.base.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TopBarDeleteButton(
    showDeleteButton: Boolean,
    onDeleteClick: () -> Unit
) {
    // Reserve space for the delete button even when it's not visible
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(48.dp), // Match the size of the IconButton
        contentAlignment = Alignment.Center
    ) {
        if (showDeleteButton) {
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Rounded.Delete, contentDescription = "Delete")
            }
        }
    }
}