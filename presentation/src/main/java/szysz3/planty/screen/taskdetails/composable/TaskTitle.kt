package szysz3.planty.screen.taskdetails.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import szysz3.planty.theme.dimensions

/**
 * A composable that displays an editable text field for a task title.
 * Uses transparent background and no indicator for a clean look.
 *
 * @param title Current text value of the title
 * @param onTitleChange Callback invoked when the title text changes
 * @param focusRequester Handles the focus state of the text field
 */
@Composable
fun TaskTitle(
    title: String,
    onTitleChange: (String) -> Unit,
    focusRequester: FocusRequester
) {
    TextField(
        value = title,
        onValueChange = onTitleChange,
        textStyle = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        placeholder = {
            Text(
                text = "Title",
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        modifier = Modifier
            .padding(MaterialTheme.dimensions().spacing16)
            .focusRequester(focusRequester),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}