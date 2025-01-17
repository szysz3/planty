package szysz3.planty.screen.mygarden.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import szysz3.planty.R

@Composable
fun GardenBreadcrumb(
    modifier: Modifier = Modifier,
    gardenPath: List<Int>,
    onNavigate: (Int?) -> Unit,
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(gardenPath) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (gardenPath.size > 1) {
            gardenPath.forEachIndexed { index, gardenId ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + expandHorizontally(
                        animationSpec = tween(300),
                        expandFrom = Alignment.Start
                    ),
                    exit = fadeOut(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + shrinkHorizontally(
                        animationSpec = tween(300),
                        shrinkTowards = Alignment.End
                    )
                ) {
                    BreadcrumbItem(
                        label = "Garden-$gardenId",
                        gardenId = gardenId,
                        onNavigate = onNavigate,
                        isLastItem = index == gardenPath.size - 1
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
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = Modifier
            .height(36.dp)
            .scale(if (isPressed) 0.95f else 1f)
            .alpha(if (isLastItem) 1f else 0.5f)
            .clickable(
                interactionSource = interactionSource,
                indication = null // Remove default ripple
            ) {
                onNavigate(gardenId)
            }
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
                .padding(horizontal = 12.dp)
                .align(Alignment.Center),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}