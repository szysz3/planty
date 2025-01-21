package szysz3.planty.core.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * A composable that displays an image and text horizontally aligned.
 * The image is placed on the left side with the text following it.
 *
 * @param imageRes Resource ID of the image to be displayed
 * @param title Text to be displayed next to the image
 * @param iconSize Size of the image in dp (default: 36)
 * @param textStyle Style to be applied to the text (default: MaterialTheme.typography.titleMedium)
 * @param modifier Modifier to be applied to the composable (default: Modifier)
 */
@Composable
fun ImageWithTextHorizontal(
    imageRes: Int,
    title: String,
    iconSize: Int = 36,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            modifier = Modifier.size(iconSize.dp)
        )
        Text(
            overflow = TextOverflow.Ellipsis,
            text = title,
            style = textStyle
        )
    }
}