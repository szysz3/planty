package szysz3.planty.screen.plantdetails.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import szysz3.planty.core.composable.ImageButton

@Composable
fun WebSearchButton(
    onClick: () -> Unit
) {
    ImageButton(
        icon = Icons.Rounded.Search,
        text = "More details",
        modifier = Modifier
            .padding(8.dp)
    ) {
        onClick()
    }
}