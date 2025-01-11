package szysz3.planty.screen.mygarden.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun CreateSubGardenDialog(
    onDismiss: () -> Unit,
    onConfirm: (rows: Int, columns: Int) -> Unit
) {
    var rows by remember { mutableStateOf("") }
    var columns by remember { mutableStateOf("") }
    var rowsError by remember { mutableStateOf(false) }
    var columnsError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Create Sub-garden")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = rows,
                    onValueChange = {
                        rows = it
                        rowsError = it.toIntOrNull()?.let { value ->
                            value <= 0 || value > 20
                        } ?: true
                    },
                    label = { Text("Rows (1-20)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = rowsError,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = columns,
                    onValueChange = {
                        columns = it
                        columnsError = it.toIntOrNull()?.let { value ->
                            value <= 0 || value > 20
                        } ?: true
                    },
                    label = { Text("Columns (1-20)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = columnsError,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val rowsInt = rows.toIntOrNull() ?: 0
                    val columnsInt = columns.toIntOrNull() ?: 0
                    if (rowsInt in 1..20 && columnsInt in 1..20) {
                        onConfirm(rowsInt, columnsInt)
                    }
                },
                enabled = !rowsError && !columnsError && rows.isNotEmpty() && columns.isNotEmpty()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}