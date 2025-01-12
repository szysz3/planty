package szysz3.planty.screen.mygarden.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GardenEditToolbar(
    onConfirmMerge: () -> Unit,
    onCancelEdit: () -> Unit,
    isMergeEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onCancelEdit) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cancel"
            )
        }

        FilledTonalButton(
            onClick = onConfirmMerge,
            enabled = isMergeEnabled
        ) {
            Text("Merge Cells")
        }
    }
}