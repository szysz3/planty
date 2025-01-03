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

@Composable
fun TaskCostField(
    initialValue: String?,
    onCostChange: (Float?) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(initialValue.orEmpty()) }

    TextField(
        modifier = modifier,
        value = text,
        onValueChange = { newValue ->
            val sanitizedInput = newValue.replace(',', '.')

            val decimalRegex = Regex("^\\d*\\.?\\d*\$")
            if (sanitizedInput.isEmpty() || sanitizedInput.matches(decimalRegex)) {
                text = sanitizedInput
                onCostChange(sanitizedInput.toFloatOrNull())
            }
        },
        placeholder = {
            Text(text = "$10.00")
        },
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}
