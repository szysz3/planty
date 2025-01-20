package szysz3.planty.screen.imagegallery.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import szysz3.planty.R

/**
 * A zoomable image component that loads and displays an image from a URL.
 * Supports zoom gestures and displays a placeholder while loading.
 *
 * @param imageUrl URL of the image to display
 */
@Composable
fun ZoomableImage(
    imageUrl: String,
) {
    val zoomState = rememberZoomState()
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .placeholder(R.drawable.plant_placeholder)
            .error(R.drawable.plant_placeholder)
            .build(),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .zoomable(
                zoomState = zoomState,
            )
    )
}