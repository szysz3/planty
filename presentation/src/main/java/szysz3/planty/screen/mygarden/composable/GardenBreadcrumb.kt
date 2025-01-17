package szysz3.planty.screen.mygarden.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            gardenPath.forEach { gardenId ->
                BreadcrumbItem(
                    label = "Garden-$gardenId",
                    gardenId = gardenId,
                    onNavigate = onNavigate
                )
            }
        }
    }
}

@Composable
private fun BreadcrumbItem(label: String, gardenId: Int?, onNavigate: (Int?) -> Unit) {
    Box(
        modifier = Modifier
            .height(36.dp)
            .clickable { onNavigate(gardenId) }
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
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}