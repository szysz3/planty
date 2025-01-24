package szysz3.planty.screen.plantdetails.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import szysz3.planty.core.composable.ImageButton
import szysz3.planty.theme.dimensions

/**
 * A search button with an icon that triggers a web search when clicked.
 *
 * @param onClick Callback invoked when the button is clicked
 */
@Composable
fun WebSearchButton(
    onClick: () -> Unit
) {
    ImageButton(
        icon = Icons.Rounded.Search,
        text = "More details",
        modifier = Modifier
            .padding(MaterialTheme.dimensions().spacing8)
    ) {
        onClick()
    }
}