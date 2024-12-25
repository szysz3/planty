package szysz3.planty.screen.tasklist.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import szysz3.planty.screen.tasklist.model.SubTask
import szysz3.planty.screen.tasklist.model.Task

@Composable
fun TaskCard(
    task: Task,
    onSubTaskCheckedChanged: (Task, SubTask, Boolean) -> Unit,
    onEditClicked: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    val isTaskCompleted = task.tasks.all { it.isCompleted }
    val completedSubTasks = task.tasks.filter { it.isCompleted }
    val incompleteSubTasks = task.tasks.filterNot { it.isCompleted }

    // Combine subtasks while limiting the display to 3 items
    val displayedSubTasks = (incompleteSubTasks + completedSubTasks).take(3)
    val hasMoreItems = (incompleteSubTasks + completedSubTasks).size > 3

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (isTaskCompleted) {
                MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Task title
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Render subtasks
                displayedSubTasks.forEachIndexed { index, subTask ->
                    SubTaskRow(
                        subTask = subTask,
                        task = task,
                        onSubTaskCheckedChanged = onSubTaskCheckedChanged
                    )

                    // Reserve space for divider, even if not shown
                    if (index == incompleteSubTasks.size - 1 && completedSubTasks.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .padding(vertical = 8.dp)
                        ) {
                            if (incompleteSubTasks.isNotEmpty()) {
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = 0.2f
                                    )
                                )
                            }
                        }
                    }
                }

                // "More items" indicator
                if (hasMoreItems) {
                    Text(
                        text = "+${(incompleteSubTasks + completedSubTasks).size - 3} more",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Edit icon in top-right corner
            IconButton(
                onClick = { onEditClicked(task) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit task",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun SubTaskRow(
    subTask: SubTask,
    task: Task,
    onSubTaskCheckedChanged: (Task, SubTask, Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = subTask.isCompleted,
            onCheckedChange = { isChecked ->
                onSubTaskCheckedChanged(task, subTask, isChecked)
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = subTask.description,
            style = MaterialTheme.typography.bodyMedium.copy(
                textDecoration = if (subTask.isCompleted) {
                    TextDecoration.LineThrough
                } else {
                    TextDecoration.None
                }
            ),
            color = if (subTask.isCompleted) {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}
