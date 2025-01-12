package szysz3.planty.screen.mygarden.composable

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GardenBreadcrumb(
    gardenPath: List<Int>,
    onNavigate: (Int?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BreadcrumbItem(label = "Main Garden", gardenId = null, onNavigate = onNavigate)

        gardenPath.forEach { gardenId ->
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            BreadcrumbItem(
                label = "Sub-garden $gardenId",
                gardenId = gardenId,
                onNavigate = onNavigate
            )
        }
    }
}

@Composable
private fun BreadcrumbItem(label: String, gardenId: Int?, onNavigate: (Int?) -> Unit) {
    TextButton(
        onClick = { onNavigate(gardenId) },
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(label)
    }
}