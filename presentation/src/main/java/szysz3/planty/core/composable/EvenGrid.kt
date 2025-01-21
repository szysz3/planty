package szysz3.planty.core.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Creates a grid layout with evenly distributed items across the specified number of columns.
 * Empty spaces in the last row are filled with spacers to maintain even distribution.
 *
 * @param T The type of items in the input list
 * @param E The type of composable elements created for each item
 * @param items List of items to be displayed in the grid
 * @param columns Number of columns in the grid
 * @param modifier Optional modifier for the grid layout
 * @param createItem Composable function that creates a UI element for each item
 */
@Composable
fun <T, E> EvenGrid(
    items: List<T>,
    columns: Int,
    modifier: Modifier = Modifier,
    createItem: @Composable (T) -> E
) {
    Column(modifier = modifier) {
        items.chunked(columns).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { item ->
                    createItem(item)
                }

                repeat(columns - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}