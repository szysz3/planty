package szysz3.planty.screen.taskdetails.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import szysz3.planty.core.model.SubTask

@Composable
fun SubTaskRow(
    modifier: Modifier = Modifier,
    subTask: SubTask,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    onDescriptionChange: ((String) -> Unit)? = null,
    onCostChange: ((Float?) -> Unit)? = null
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
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(end = 4.dp),
            value = subTask.description,
            onValueChange = { newText -> onDescriptionChange?.invoke(newText) },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                textDecoration = if (subTask.isCompleted) TextDecoration.LineThrough else null
            ),
            placeholder = {
                Text(
                    text = "Sub task title",
                    textDecoration = if (subTask.isCompleted) TextDecoration.LineThrough else null,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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
        Spacer(modifier = Modifier.width(4.dp))
        VerticalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            thickness = 1.dp,
            modifier = Modifier
                .height(18.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        CostTextField(
            modifier = Modifier.padding(start = 4.dp),
            initialValue = subTask.cost?.toString() ?: "",
            onCostChange = {
                onCostChange?.invoke(it)
            })
    }
}