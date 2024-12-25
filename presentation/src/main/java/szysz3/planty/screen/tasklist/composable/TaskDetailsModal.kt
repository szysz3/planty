package szysz3.planty.screen.tasklist.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import szysz3.planty.screen.tasklist.model.SubTask
import szysz3.planty.screen.tasklist.model.Task

@Composable
fun TaskDetailsModal(
    task: Task?,
    onTaskUpdated: (Task) -> Unit,
    onTaskDeleted: (Task) -> Unit,
    onDismiss: () -> Unit,
    visible: Boolean
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        task?.let { task ->
            Box(
                modifier = Modifier
                    .fillMaxSize() // Full-screen overlay
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f)) // Dimming background
                    .clickable(
                        enabled = true,
                        onClick = { onDismiss() } // Dismiss on outside click
                    )
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center) // Center the modal
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 32.dp
                        ) // Padding for modal boundaries
                ) {
                    Surface(
                        shape = MaterialTheme.shapes.large,
                        tonalElevation = 8.dp,
                        modifier = Modifier
                            .fillMaxWidth() // Ensure full width
                            .wrapContentHeight()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp) // Inner padding for content
                        ) {
                            // Task title and delete button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = task.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = { onTaskDeleted(task) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Task"
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Subtasks
                            val completedSubTasks = task.tasks.filter { it.isCompleted }
                            val incompleteSubTasks = task.tasks.filterNot { it.isCompleted }

                            incompleteSubTasks.forEach { subTask ->
                                SubTaskRow(
                                    subTask = subTask,
                                    onSubTaskCheckedChanged = { updatedSubTask, isChecked ->
                                        val updatedSubTasks = task.tasks.map {
                                            if (it == updatedSubTask) it.copy(isCompleted = isChecked) else it
                                        }
                                        onTaskUpdated(task.copy(tasks = updatedSubTasks))
                                    }
                                )
                            }

                            if (incompleteSubTasks.isNotEmpty() && completedSubTasks.isNotEmpty()) {
                                HorizontalDivider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                )
                            }

                            completedSubTasks.forEach { subTask ->
                                SubTaskRow(
                                    subTask = subTask,
                                    onSubTaskCheckedChanged = { updatedSubTask, isChecked ->
                                        val updatedSubTasks = task.tasks.map {
                                            if (it == updatedSubTask) it.copy(isCompleted = isChecked) else it
                                        }
                                        onTaskUpdated(task.copy(tasks = updatedSubTasks))
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Total cost
                            val totalCost = task.tasks.sumOf { it.cost }
                            if (totalCost > 0) {
                                Text(
                                    text = "Total Cost: $totalCost",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }

                            Button(
                                onClick = {
                                    onTaskUpdated(task)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Save Changes")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SubTaskRow(
    subTask: SubTask,
    onSubTaskCheckedChanged: (SubTask, Boolean) -> Unit
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
                onSubTaskCheckedChanged(subTask, isChecked)
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
