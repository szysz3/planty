package szysz3.planty.screen.tasklist.composable

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
import szysz3.planty.core.model.Task

@Composable
fun TaskCardView(
    task: Task,
    modifier: Modifier = Modifier,
    onClicked: (Task?) -> Unit,
) {
    val completedSubTasks = task.subTasks.count { it.isCompleted }
    val totalSubTasks = task.subTasks.size
    val completedCost = task.subTasks.filter { it.isCompleted }.sumOf { it.cost?.toDouble() ?: 0.0 }
    val totalCost = task.subTasks.sumOf { it.cost?.toDouble() ?: 0.0 }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(8.dp)
            .alpha(
                if (completedSubTasks == totalSubTasks) {
                    0.6f
                } else {
                    1f
                }
            )
            .clip(MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = { onClicked(task) }
    ) {

        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.25f)
                    .background(color = task.color)
                    .clip(MaterialTheme.shapes.medium)
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 32.dp, top = 8.dp)
                ) {
                    Text(
                        text = "$completedSubTasks",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Text(
                        text = " / ",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Text(
                        text = "$totalSubTasks",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }
            }

            if (totalCost > 0) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 16.dp)
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
        }
    }
}