package szysz3.planty.screen.taskdetails.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import szysz3.planty.theme.dimensions

/**
 * A composable button that allows adding new subtasks.
 * Displays as a text with "+" prefix in a row layout.
 *
 * @param onClick Callback invoked when the button is clicked
 */
@Composable
fun NewTaskButton(onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.dimensions().spacing16)
    ) {
        Text(
            "+ New subtask",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            modifier = Modifier.clickable {
                onClick()
            }
        )
    }
}