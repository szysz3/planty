package szysz3.planty.screen.mygarden.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import szysz3.planty.screen.mygarden.model.GardenState
import szysz3.planty.screen.mygarden.model.MergedCell

private object AnimationConstants {
    const val GARDEN_FADE_DURATION_MS = 300
    const val SINGLE_CELL_FADE_DURATION_MS = 200
    const val GARDEN_SCALE_INITIAL = 0.95f
    const val SINGLE_CELL_SCALE_INITIAL = 0.9f
    const val GARDEN_SCALE_OUT_TARGET = 1.05f
}

@Composable
fun GardenMap(
    modifier: Modifier = Modifier,
    gardenRows: Int,
    gardenColumns: Int,
    gardenState: GardenState,
    isEditMode: Boolean = false,
    selectedCellPositions: Set<Pair<Int, Int>> = emptySet(),
    onCellClick: (rowIndex: Int, columnIndex: Int) -> Unit,
    onMergedCellClick: (mergedCell: MergedCell) -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val individualCellSize = minOf(maxWidth / gardenColumns, 100.dp)

        Box(
            modifier = Modifier
                .width(individualCellSize * gardenColumns)
                .height(individualCellSize * gardenRows)
                .align(Alignment.Center)
        ) {
            AnimatedContent(
                targetState = gardenState,
                transitionSpec = {
                    ContentTransform(
                        targetContentEnter = fadeIn(tween(AnimationConstants.GARDEN_FADE_DURATION_MS)) +
                                scaleIn(
                                    initialScale = AnimationConstants.GARDEN_SCALE_INITIAL,
                                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                                ),
                        initialContentExit = fadeOut(tween(AnimationConstants.GARDEN_FADE_DURATION_MS)) +
                                scaleOut(targetScale = AnimationConstants.GARDEN_SCALE_OUT_TARGET),
                        sizeTransform = SizeTransform(clip = false)
                    )
                }
            ) { currentGardenState ->
                GardenContent(
                    gardenRows = gardenRows,
                    gardenColumns = gardenColumns,
                    cellSize = individualCellSize,
                    gardenState = currentGardenState,
                    isEditMode = isEditMode,
                    selectedCellPositions = selectedCellPositions,
                    onCellClick = onCellClick,
                    onMergedCellClick = onMergedCellClick
                )
            }
        }
    }
}

@Composable
private fun GardenContent(
    gardenRows: Int,
    gardenColumns: Int,
    cellSize: androidx.compose.ui.unit.Dp,
    gardenState: GardenState,
    isEditMode: Boolean,
    selectedCellPositions: Set<Pair<Int, Int>>,
    onCellClick: (rowIndex: Int, columnIndex: Int) -> Unit,
    onMergedCellClick: (mergedCell: MergedCell) -> Unit
) {
    val occupiedCellPositions = remember(gardenState.mergedCells) {
        buildOccupiedCellsMap(gardenState.mergedCells)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(cellSize * gardenRows)
    ) {
        for (rowIndex in 0 until gardenRows) {
            for (columnIndex in 0 until gardenColumns) {
                val currentPosition = rowIndex to columnIndex
                if (currentPosition in occupiedCellPositions) continue

                key(rowIndex, columnIndex) {
                    val mergedCellAtPosition = gardenState.mergedCells.find { mergedCell ->
                        rowIndex in mergedCell.startRow..mergedCell.endRow &&
                                columnIndex in mergedCell.startColumn..mergedCell.endColumn
                    }

                    when {
                        mergedCellAtPosition != null &&
                                rowIndex == mergedCellAtPosition.startRow &&
                                columnIndex == mergedCellAtPosition.startColumn -> {
                            AnimatedContent(
                                targetState = mergedCellAtPosition,
                                transitionSpec = {
                                    ContentTransform(
                                        targetContentEnter = fadeIn(tween(AnimationConstants.GARDEN_FADE_DURATION_MS)) +
                                                scaleIn(
                                                    initialScale = AnimationConstants.GARDEN_SCALE_INITIAL,
                                                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                                                ),
                                        initialContentExit = fadeOut(tween(AnimationConstants.GARDEN_FADE_DURATION_MS)),
                                        sizeTransform = SizeTransform(clip = false)
                                    )
                                }
                            ) { currentMergedCell ->
                                GardenMergedCellBox(
                                    mergedCell = currentMergedCell,
                                    cellSize = cellSize,
                                    onClick = { onMergedCellClick(currentMergedCell) }
                                )
                            }
                        }

                        mergedCellAtPosition == null -> {
                            AnimatedContent(
                                targetState = Triple(
                                    currentPosition in selectedCellPositions,
                                    isEditMode,
                                    gardenState
                                ),
                                transitionSpec = {
                                    ContentTransform(
                                        targetContentEnter = fadeIn(tween(AnimationConstants.SINGLE_CELL_FADE_DURATION_MS)) +
                                                scaleIn(
                                                    initialScale = AnimationConstants.SINGLE_CELL_SCALE_INITIAL,
                                                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                                                ),
                                        initialContentExit = fadeOut(tween(AnimationConstants.SINGLE_CELL_FADE_DURATION_MS)),
                                        sizeTransform = SizeTransform(clip = false)
                                    )
                                }
                            ) { (isSelected, isInEditMode, currentGardenState) ->
                                GardenCellBox(
                                    row = rowIndex,
                                    col = columnIndex,
                                    cellSize = cellSize,
                                    isSelected = isSelected,
                                    isEditMode = isInEditMode,
                                    state = currentGardenState,
                                    onClick = { onCellClick(rowIndex, columnIndex) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun buildOccupiedCellsMap(mergedCells: List<MergedCell>): Set<Pair<Int, Int>> =
    buildSet {
        mergedCells.forEach { mergedCell ->
            for (rowIndex in mergedCell.startRow..mergedCell.endRow) {
                for (columnIndex in mergedCell.startColumn..mergedCell.endColumn) {
                    if (rowIndex != mergedCell.startRow || columnIndex != mergedCell.startColumn) {
                        add(rowIndex to columnIndex)
                    }
                }
            }
        }
    }