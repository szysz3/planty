package szysz3.planty.screen.mygarden.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import szysz3.planty.screen.mygarden.model.MergedCell
import szysz3.planty.theme.Shapes
import szysz3.planty.theme.dimensions

/**
 * Displays a merged cell in the garden grid that shows either existing sub-garden info or an empty state.
 *
 * @param mergedCell Cell information including position, size and sub-garden details
 * @param cellSize Base size of a single grid cell
 * @param onClick Called when the cell is clicked
 */
@Composable
fun GardenMergedCellBox(
    mergedCell: MergedCell,
    cellSize: Dp,
    onClick: () -> Unit
) {
    val dimens = MaterialTheme.dimensions()
    Box(
        modifier = Modifier
            .offset(
                x = cellSize * mergedCell.startColumn,
                y = cellSize * mergedCell.startRow
            )
            .size(cellSize * mergedCell.width, cellSize * mergedCell.height)
            .padding(dimens.spacing2)
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                shape = Shapes.extraSmall
            )
            .border(
                width = dimens.size2,
                color = MaterialTheme.colorScheme.primary,
                shape = Shapes.extraSmall
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        GardenCellContent(mergedCell)
    }
}

@Composable
private fun GardenCellContent(mergedCell: MergedCell) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        modifier = Modifier.padding(MaterialTheme.dimensions().spacing8)
    ) {
        if (mergedCell.subGardenId != null) {
            mergedCell.subGardenName?.let { name ->
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (mergedCell.subGardenRows != null && mergedCell.subGardenColumns != null) {
                Text(
                    text = "${mergedCell.subGardenColumns}×${mergedCell.subGardenRows}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                )
            }
        } else {
            Text(
                text = "Tap to create\nsub-garden",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Text(
                text = "${mergedCell.width}×${mergedCell.height}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
            )
        }
    }
}