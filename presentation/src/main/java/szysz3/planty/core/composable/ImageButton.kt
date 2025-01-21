package szysz3.planty.core.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * A composable that combines an [IconButton] with accompanying text in a horizontal layout.
 * The text appears to the right of the icon and both elements share the same semi-transparent color.
 *
 * @param icon The [ImageVector] to be displayed as the button's icon
 * @param text The text label to be displayed next to the icon
 * @param modifier Optional [Modifier] for customizing the layout
 * @param onClick Callback invoked when the button is clicked
 */
@Composable
fun ImageButton(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                onClick()
            }) {
            Icon(
                imageVector = icon,
                contentDescription = "icon",
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}