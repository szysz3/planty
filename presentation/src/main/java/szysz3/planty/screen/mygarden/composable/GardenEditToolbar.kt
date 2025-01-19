package szysz3.planty.screen.mygarden.composable

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

private const val TOOLBAR_ANIMATION_DURATION = 300

/**
 * A toolbar for garden editing operations with cell merge functionality.
 * Animates its visibility and supports enabling/disabling the merge action.
 *
 * @param onConfirmMerge Called when merge action is confirmed
 * @param onCancelEdit Called when editing is cancelled
 * @param isMergeEnabled Whether the merge action is available
 * @param isVisible Controls toolbar visibility with animation
 * @param modifier Optional modifier for the layout
 */
@Composable
fun GardenEditToolbar(
    onConfirmMerge: () -> Unit,
    onCancelEdit: () -> Unit,
    isMergeEnabled: Boolean,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = TOOLBAR_ANIMATION_DURATION),
        label = "toolbar_alpha"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp)
            .alpha(alpha),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onCancelEdit,
            enabled = isVisible
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cancel"
            )
        }

        FilledTonalButton(
            onClick = onConfirmMerge,
            enabled = isVisible && isMergeEnabled
        ) {
            Text("Merge Cells")
        }
    }
}