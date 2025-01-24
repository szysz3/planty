package szysz3.planty.screen.mygarden.composable.input

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import szysz3.planty.core.composable.RoundedButton
import szysz3.planty.theme.Shapes
import szysz3.planty.theme.dimensions

private object GardenInputConstants {
    const val MAX_NAME_LENGTH = 10
    const val DEFAULT_MAX_DIMENSION = 20
    const val DEFAULT_MIN_DIMENSION = 2
}

/**
 * A bottom sheet composable that allows users to input garden dimensions and name.
 *
 * @param bottomSheetState The state of the bottom sheet
 * @param maxDimension Maximum allowed dimension for width and height
 * @param onDismissRequest Callback invoked when the bottom sheet is dismissed
 * @param onDimensionsSubmitted Callback invoked when valid dimensions are submitted
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GardenDimensionsInput(
    bottomSheetState: SheetState,
    maxDimension: Int = GardenInputConstants.DEFAULT_MAX_DIMENSION,
    onDismissRequest: () -> Unit,
    onDimensionsSubmitted: (String, Int, Int) -> Unit
) {
    var inputState by remember { mutableStateOf(GardenDimensionInputState()) }

    ModalBottomSheet(
        sheetState = bottomSheetState,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimensions().spacing16),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Enter Garden Details", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(MaterialTheme.dimensions().spacing20))

            GardenInputContent(
                inputState = inputState,
                maxDimension = maxDimension,
                onInputStateChanged = { inputState = it },
                onDimensionsSubmitted = onDimensionsSubmitted
            )
        }
    }
}

@Composable
private fun GardenInputContent(
    inputState: GardenDimensionInputState,
    maxDimension: Int,
    onInputStateChanged: (GardenDimensionInputState) -> Unit,
    onDimensionsSubmitted: (String, Int, Int) -> Unit
) {
    TextField(
        value = inputState.name,
        onValueChange = { input ->
            if (input.length <= GardenInputConstants.MAX_NAME_LENGTH) {
                onInputStateChanged(
                    inputState.copy(
                        name = input.filter { it.isLetterOrDigit() },
                        nameError = input.isEmpty()
                    )
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.medium),
        label = { Text("Name (max ${GardenInputConstants.MAX_NAME_LENGTH} characters)") },
        singleLine = true,
        isError = inputState.nameError
    )
    if (inputState.nameError) {
        Text(
            text = "Garden name is required",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
    }
    Spacer(modifier = Modifier.height(MaterialTheme.dimensions().spacing12))

    // Width Input
    TextField(
        value = inputState.width,
        onValueChange = { input ->
            onInputStateChanged(
                inputState.copy(
                    width = input,
                    widthError = input.toIntOrNull()?.let { it > maxDimension } ?: false
                )
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.medium),
        label = { Text("Width (in cells, max $maxDimension)") },
        singleLine = true,
        isError = inputState.widthError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    if (inputState.widthError) {
        Text(
            text = "Width cannot exceed $maxDimension",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
    }
    Spacer(modifier = Modifier.height(MaterialTheme.dimensions().spacing12))

    // Height Input
    TextField(
        value = inputState.height,
        onValueChange = { input ->
            onInputStateChanged(
                inputState.copy(
                    height = input,
                    heightError = input.toIntOrNull()?.let { it > maxDimension } ?: false
                )
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.medium),
        label = { Text("Height (in cells, max $maxDimension)") },
        singleLine = true,
        isError = inputState.heightError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    if (inputState.heightError) {
        Text(
            text = "Height cannot exceed $maxDimension",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
    }

    Spacer(modifier = Modifier.height(MaterialTheme.dimensions().spacing20))

    val width = inputState.width.toIntOrNull() ?: GardenInputConstants.DEFAULT_MIN_DIMENSION
    val height = inputState.height.toIntOrNull() ?: GardenInputConstants.DEFAULT_MIN_DIMENSION
    val isEnabled = !inputState.nameError && !inputState.widthError &&
            !inputState.heightError && inputState.name.isNotEmpty()

    RoundedButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            if (width >= GardenInputConstants.DEFAULT_MIN_DIMENSION &&
                height >= GardenInputConstants.DEFAULT_MIN_DIMENSION
            ) {
                onDimensionsSubmitted(inputState.name, height, width)
            }
        },
        enabled = isEnabled,
        text = "Confirm"
    )
}