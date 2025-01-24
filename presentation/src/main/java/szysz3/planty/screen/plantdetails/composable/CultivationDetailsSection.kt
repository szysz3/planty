package szysz3.planty.screen.plantdetails.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import szysz3.planty.R
import szysz3.planty.core.composable.ImageWithTextHorizontal
import szysz3.planty.theme.dimensions

/**
 * A composable section that displays cultivation details for a plant.
 * This component shows an info icon with a title, followed by detailed cultivation instructions.
 *
 * @param details The cultivation instructions text to be displayed.
 */
@Composable
fun CultivationDetailsSection(details: String) {
    val dimens = MaterialTheme.dimensions()
    Column {
        ImageWithTextHorizontal(
            imageRes = R.drawable.icon_info,
            title = "Cultivation details",
            iconSize = dimens.size36,
            textStyle = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(dimens.size4))
        Text(
            text = details,
            modifier = Modifier.padding(dimens.spacing8)
        )
    }
}