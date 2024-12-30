package szysz3.planty.screen.mygarden.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import szysz3.planty.core.composable.RoundedButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GardenDimensionsInput(
    bottomSheetState: SheetState,
    maxDimension: Int = 50,
    onDismissRequest: () -> Unit,
    onDimensionsSubmitted: (Int, Int) -> Unit
) {
    var widthInput by remember { mutableStateOf("") }
    var heightInput by remember { mutableStateOf("") }
    var widthError by remember { mutableStateOf(false) }
    var heightError by remember { mutableStateOf(false) }

    ModalBottomSheet(
        sheetState = bottomSheetState,
        onDismissRequest = { onDismissRequest() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Enter Garden Dimensions", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            // Width Input
            TextField(
                value = widthInput,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { input ->
                    widthInput = input
                    widthError = input.toIntOrNull()?.let { it > maxDimension } ?: false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                label = { Text("Width (in cells, max $maxDimension)") },
                singleLine = true,
                isError = widthError
            )
            if (widthError) {
                Text(
                    text = "Width cannot exceed $maxDimension",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Height Input
            TextField(
                value = heightInput,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { input ->
                    heightInput = input
                    heightError = input.toIntOrNull()?.let { it > maxDimension } ?: false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                label = { Text("Height (in cells, max $maxDimension)") },
                singleLine = true,
                isError = heightError
            )
            if (heightError) {
                Text(
                    text = "Height cannot exceed $maxDimension",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            RoundedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val width = widthInput.toIntOrNull() ?: 1
                    val height = heightInput.toIntOrNull() ?: 1

                    if (!widthError && !heightError && width > 0 && height > 0) {
                        onDimensionsSubmitted(height, width)
                    }
                },
                enabled = !widthError && !heightError,
                text = "Confirm"
            )
        }
    }
}
