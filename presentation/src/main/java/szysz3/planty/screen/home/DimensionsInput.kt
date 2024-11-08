package szysz3.planty.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import szysz3.planty.ui.widgets.RoundedButton

@Composable
fun DimensionsInput(onDimensionsSubmitted: (MapDimensions) -> Unit) {
    var widthInput by remember { mutableStateOf("") }
    var heightInput by remember { mutableStateOf("") }
    var widthError by remember { mutableStateOf(false) }
    var heightError by remember { mutableStateOf(false) }

    val maxDimension = 50

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Enter Garden Dimensions", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = widthInput,
            onValueChange = { input ->
                widthInput = input
                widthError = input.toIntOrNull()?.let { it > maxDimension } ?: false
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface),
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

        TextField(
            value = heightInput,
            onValueChange = { input ->
                heightInput = input
                heightError = input.toIntOrNull()?.let { it > maxDimension } ?: false
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface),
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
            onClick = {
                val width = widthInput.toIntOrNull() ?: 1
                val height = heightInput.toIntOrNull() ?: 1

                if (width <= maxDimension && height <= maxDimension) {
                    onDimensionsSubmitted(MapDimensions(width, height))
                }
            },
            enabled = !widthError && !heightError,
            text = "Confirm"
        )
    }
}