package szysz3.planty.screen.taskdetails.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import szysz3.planty.R
import szysz3.planty.core.model.SubTask
import szysz3.planty.theme.dimensions

/**
 * A composable that displays a subtask row with a checkbox, description field and cost field.
 *
 * @param modifier Modifier to be applied to the row
 * @param subTask The subtask data to be displayed
 * @param onCheckedChange Callback invoked when the checkbox state changes
 * @param onDescriptionChange Callback invoked when the description text changes
 * @param onCostChange Callback invoked when the cost value changes
 */
@Composable
fun SubTaskRow(
    modifier: Modifier = Modifier,
    subTask: SubTask,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    onDescriptionChange: ((String) -> Unit)? = null,
    onCostChange: ((Float?) -> Unit)? = null
) {
    val dimens = MaterialTheme.dimensions()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Checkbox(
            checked = subTask.isCompleted,
            onCheckedChange = onCheckedChange,
            enabled = !subTask.isCompleted
        )

        SubTaskDescription(
            description = subTask.description,
            isCompleted = subTask.isCompleted,
            onDescriptionChange = onDescriptionChange
        )

        Spacer(modifier = Modifier.width(dimens.size4))
        VerticalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            thickness = dimens.size1,
            modifier = Modifier.height(dimens.size20)
        )
        Spacer(modifier = Modifier.width(dimens.size4))

        TaskCostField(
            modifier = Modifier.padding(start = dimens.spacing4),
            initialValue = subTask.cost?.toString() ?: "",
            onCostChange = onCostChange
        )
    }
}

@Composable
private fun SubTaskDescription(
    description: String,
    isCompleted: Boolean,
    onDescriptionChange: ((String) -> Unit)?
) {
    val textDecoration = if (isCompleted) TextDecoration.LineThrough else null

    TextField(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .padding(end = MaterialTheme.dimensions().spacing4),
        value = description,
        onValueChange = { newText -> onDescriptionChange?.invoke(newText) },
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface,
            textDecoration = textDecoration
        ),
        placeholder = {
            Text(
                text = stringResource(R.string.subtask_title_placeholder),
                textDecoration = textDecoration,
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
}