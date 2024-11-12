package szysz3.planty.screen.home.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteGardenDialog(onConfirmDelete: () -> Unit, onCancel: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(text = "Confirm Delete") },
        text = { Text(text = "Are you sure you want to delete your garden map?") },
        confirmButton = { TextButton(onClick = onConfirmDelete) { Text("Delete") } },
        dismissButton = { TextButton(onClick = onCancel) { Text("Cancel") } }
    )
}