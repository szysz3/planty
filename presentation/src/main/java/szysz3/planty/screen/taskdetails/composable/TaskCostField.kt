package szysz3.planty.screen.taskdetails.composable

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

private val DECIMAL_REGEX = Regex("^\\d*\\.?\\d*\$")
private const val DEFAULT_PLACEHOLDER = "$10.00"

/**
 * A composable that provides a text field for entering monetary values.
 * Handles decimal input with comma-to-period conversion and validation.
 *
 * @param initialValue Initial cost value to display
 * @param onCostChange Callback invoked when the cost value changes
 * @param modifier Modifier to be applied to the TextField
 * @param placeholder Placeholder text to display when the field is empty
 */
@Composable
fun TaskCostField(
    initialValue: String?,
    onCostChange: ((Float?) -> Unit)?,
    modifier: Modifier = Modifier,
    placeholder: String = DEFAULT_PLACEHOLDER
) {
    var text by remember { mutableStateOf(initialValue.orEmpty()) }

    TextField(
        modifier = modifier,
        value = text,
        onValueChange = { newValue -> handleCostInput(newValue, onCostChange) { text = it } },
        placeholder = { Text(text = placeholder) },
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        keyboardOptions = createKeyboardOptions(),
        colors = createTextFieldColors()
    )
}

private fun handleCostInput(
    newValue: String,
    onCostChange: ((Float?) -> Unit)?,
    updateText: (String) -> Unit
) {
    val sanitizedInput = newValue.replace(',', '.')

    if (sanitizedInput.isEmpty() || sanitizedInput.matches(DECIMAL_REGEX)) {
        updateText(sanitizedInput)
        onCostChange?.invoke(sanitizedInput.toFloatOrNull())
    }
}

private fun createKeyboardOptions() = KeyboardOptions.Default.copy(
    keyboardType = KeyboardType.Decimal,
    imeAction = ImeAction.Done
)

@Composable
private fun createTextFieldColors() = TextFieldDefaults.colors(
    unfocusedContainerColor = Color.Transparent,
    focusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent
)