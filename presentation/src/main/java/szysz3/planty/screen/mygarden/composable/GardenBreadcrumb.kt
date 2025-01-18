package szysz3.planty.screen.mygarden.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import szysz3.planty.R

@Composable
fun GardenBreadcrumb(
    modifier: Modifier = Modifier,
    gardenPath: List<Int>,
    onNavigate: (Int?) -> Unit,
    isVisible: Boolean = true
) {
    val scrollState = rememberScrollState()

    // Ensure state is preserved during animations
    val breadcrumbContent = remember(gardenPath) {
        gardenPath.map { it }
    }

    LaunchedEffect(breadcrumbContent) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        AnimatedVisibility(
            visible = isVisible && breadcrumbContent.size > 1,
            enter = fadeIn(animationSpec = tween(300)) +
                    slideInHorizontally(animationSpec = tween(300)) { fullWidth -> -fullWidth },
            exit = fadeOut(animationSpec = tween(300)) +
                    slideOutHorizontally(animationSpec = tween(300)) { fullWidth -> -fullWidth }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                breadcrumbContent.forEachIndexed { index, gardenId ->
                    BreadcrumbItem(
                        label = "Garden-$gardenId",
                        gardenId = gardenId,
                        onNavigate = onNavigate,
                        isLastItem = index == breadcrumbContent.lastIndex
                    )
                }
            }
        }
    }
}

@Composable
private fun BreadcrumbItem(
    label: String,
    gardenId: Int?,
    onNavigate: (Int?) -> Unit,
    isLastItem: Boolean
) {
    val itemAlpha by animateFloatAsState(
        targetValue = if (isLastItem) 1f else 0.5f,
        animationSpec = tween(durationMillis = 200),
        label = "item_alpha"
    )

    Box(
        modifier = Modifier
            .height(36.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = !isLastItem
            ) { onNavigate(gardenId) }
            .alpha(itemAlpha)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.chevron_button),
            contentDescription = null,
            modifier = Modifier.height(36.dp),
            tint = MaterialTheme.colorScheme.surface
        )
        Text(
            text = label,
            modifier = Modifier
                .padding(18.dp, 0.dp, 12.dp, 0.dp)
                .align(Alignment.Center),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}