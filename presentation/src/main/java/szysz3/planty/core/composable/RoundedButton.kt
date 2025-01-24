package szysz3.planty.core.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import szysz3.planty.theme.dimensions

/**
 * A customized Material 3 button with rounded corners and elevation.
 *
 * @param onClick Lambda to be invoked when the button is clicked
 * @param text Text to be displayed inside the button
 * @param enabled Controls the enabled state of the button. When false, the button will be grayed out and non-clickable
 * @param modifier Optional modifier for customizing the button's layout and appearance
 */
@Composable
fun RoundedButton(
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val dimensions = MaterialTheme.dimensions()
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = MaterialTheme.shapes.large,
        modifier = modifier
            .padding(dimensions.spacing16),
        enabled = enabled,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = dimensions.elevation8,
            pressedElevation = dimensions.elevation4,
        ),
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}