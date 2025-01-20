package szysz3.planty.screen.plantdetails.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import szysz3.planty.R
import szysz3.planty.core.composable.ImageWithTextHorizontal

/**
 * A composable section that displays cultivation details for a plant.
 * This component shows an info icon with a title, followed by detailed cultivation instructions.
 *
 * @param details The cultivation instructions text to be displayed.
 */
@Composable
fun CultivationDetailsSection(details: String) {
    Column {
        ImageWithTextHorizontal(
            imageRes = R.drawable.icon_info,
            title = "Cultivation details",
            iconSize = 32,
            textStyle = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = details,
            modifier = Modifier.padding(8.dp)
        )
    }
}