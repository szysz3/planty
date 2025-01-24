package szysz3.planty.screen.mygarden.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ScrollState
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
import szysz3.planty.R
import szysz3.planty.domain.model.Garden
import szysz3.planty.theme.dimensions

private object BreadcrumbConstants {
    const val ANIMATION_DURATION = 300
    const val ALPHA_ANIMATION_DURATION = 200
    const val ACTIVE_ITEM_ALPHA = 1f
    const val INACTIVE_ITEM_ALPHA = 0.5f
    const val MIN_PATH_LENGTH_FOR_VISIBILITY = 1
}

/**
 * A breadcrumb navigation component that displays the current garden hierarchy path.
 * Supports animations and click navigation to previous levels.
 *
 * @param modifier Modifier for the component
 * @param gardenPath List of gardens representing the navigation hierarchy
 * @param onNavigate Callback triggered when a breadcrumb item is clicked
 * @param isVisible Controls the visibility of the breadcrumb
 */
@Composable
fun GardenBreadcrumb(
    modifier: Modifier = Modifier,
    gardenPath: List<Garden>,
    onNavigate: (Int?) -> Unit,
    isVisible: Boolean = true
) {
    val scrollState = rememberScrollState()
    val breadcrumbContent = remember(gardenPath) { gardenPath.toList() }

    LaunchedEffect(breadcrumbContent) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Box(modifier = modifier.fillMaxWidth()) {
        AnimatedVisibility(
            visible = isVisible && breadcrumbContent.size > BreadcrumbConstants.MIN_PATH_LENGTH_FOR_VISIBILITY,
            enter = createEnterAnimation(),
            exit = createExitAnimation()
        ) {
            BreadcrumbRow(
                items = breadcrumbContent,
                onNavigate = onNavigate,
                scrollState = scrollState
            )
        }
    }
}

@Composable
private fun BreadcrumbRow(
    items: List<Garden>,
    onNavigate: (Int?) -> Unit,
    scrollState: ScrollState
) {
    val dimens = MaterialTheme.dimensions()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = dimens.spacing16, vertical = dimens.spacing8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, garden ->
            BreadcrumbItem(
                garden = garden,
                onNavigate = onNavigate,
                isLastItem = index == items.lastIndex
            )
        }
    }
}

@Composable
private fun BreadcrumbItem(
    garden: Garden,
    onNavigate: (Int?) -> Unit,
    isLastItem: Boolean
) {
    val dimens = MaterialTheme.dimensions()
    val itemAlpha by animateFloatAsState(
        targetValue = if (isLastItem) BreadcrumbConstants.ACTIVE_ITEM_ALPHA
        else BreadcrumbConstants.INACTIVE_ITEM_ALPHA,
        animationSpec = tween(durationMillis = BreadcrumbConstants.ALPHA_ANIMATION_DURATION),
        label = "item_alpha"
    )

    Box(
        modifier = Modifier
            .height(dimens.spacing36)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = !isLastItem
            ) { onNavigate(garden.id) }
            .alpha(itemAlpha)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.chevron_button),
            contentDescription = if (!isLastItem)
                "Navigate to ${garden.name.ifEmpty { "Garden-${garden.id}" }}"
            else null,
            modifier = Modifier.height(dimens.spacing36),
            tint = MaterialTheme.colorScheme.surface
        )
        Text(
            text = garden.name.ifEmpty { "Garden-${garden.id}" },
            modifier = Modifier
                .padding(horizontal = dimens.spacing16)
                .align(Alignment.Center),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

private fun createEnterAnimation() =
    fadeIn(animationSpec = tween(BreadcrumbConstants.ANIMATION_DURATION)) +
            slideInHorizontally(animationSpec = tween(BreadcrumbConstants.ANIMATION_DURATION))
            { fullWidth -> -fullWidth }

private fun createExitAnimation() =
    fadeOut(animationSpec = tween(BreadcrumbConstants.ANIMATION_DURATION)) +
            slideOutHorizontally(animationSpec = tween(BreadcrumbConstants.ANIMATION_DURATION))
            { fullWidth -> -fullWidth }