package szysz3.planty.screen.tasklist.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import szysz3.planty.screen.tasklist.model.TaskCardUiState
import szysz3.planty.theme.dimensions

/**
 * A composable that displays a task card with details including title, completion status, and optional cost information.
 *
 * @param uiState The UI state containing all necessary data to display the task card
 * @param modifier The modifier to be applied to the card
 * @param onClicked Callback invoked when the card is clicked
 */
@Composable
fun TaskCardView(
    uiState: TaskCardUiState,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit,
) {
    val dimens = MaterialTheme.dimensions()
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(dimens.spacing8)
            .alpha(if (uiState.isCompleted) 0.6f else 1f)
            .clip(MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.elevation8),
        onClick = onClicked
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.25f)
                    .background(color = uiState.color)
                    .clip(MaterialTheme.shapes.medium)
            )

            TaskDetailsContent(
                title = uiState.title,
                completedTasks = uiState.completedSubTasks,
                totalTasks = uiState.totalSubTasks,
                modifier = Modifier.weight(1f)
            )

            if (uiState.totalCost > 0) {
                CostSection(
                    completedCost = uiState.completedCost,
                    totalCost = uiState.totalCost,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
private fun TaskDetailsContent(
    title: String,
    completedTasks: Int,
    totalTasks: Int,
    modifier: Modifier = Modifier
) {
    val dimens = MaterialTheme.dimensions()
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(dimens.spacing8)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = dimens.spacing8)
        )

        Row(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = dimens.spacing32, top = dimens.spacing8)
        ) {
            Text(
                text = "$completedTasks",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Text(
                text = " / ",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Text(
                text = "$totalTasks",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun CostSection(
    completedCost: Double,
    totalCost: Double,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(end = MaterialTheme.dimensions().spacing16)
    ) {
        Text(
            text = String.format("%.2f", completedCost),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = " out of ",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = String.format("%.2f", totalCost),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}