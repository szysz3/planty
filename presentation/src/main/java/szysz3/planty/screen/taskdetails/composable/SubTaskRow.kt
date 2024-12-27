package szysz3.planty.screen.taskdetails.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import szysz3.planty.screen.tasklist.model.SubTask

@Composable
fun SubTaskRow(
    modifier: Modifier = Modifier,
    subTask: SubTask,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    onDescriptionChange: ((String) -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = subTask.isCompleted,
            onCheckedChange = onCheckedChange,
            enabled = !subTask.isCompleted
        )
        TextField(
            value = subTask.description,
            onValueChange = { newText -> onDescriptionChange?.invoke(newText) },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                textDecoration = if (subTask.isCompleted) TextDecoration.LineThrough else null
            ), placeholder = {
                Text(
                    text = "Sub task title",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}