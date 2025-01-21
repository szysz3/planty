package szysz3.planty.core.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable


/**
 * A composable that displays a Material3 alert dialog for delete confirmation.
 * The dialog includes a title, message, and two buttons for confirming or canceling the delete action.
 *
 * @param title The title text to be displayed at the top of the dialog
 * @param message The message text explaining the delete action
 * @param confirmButtonText The text for the confirm/delete button
 * @param dismissButtonText The text for the cancel/dismiss button
 * @param onConfirmDelete Callback function triggered when the delete action is confirmed
 * @param onCancel Callback function triggered when the dialog is dismissed or canceled
 */
@Composable
fun DeleteAlertDialog(
    title: String,
    message: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirmDelete: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onConfirmDelete) {
                Text(
                    confirmButtonText,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(
                    dismissButtonText,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}
