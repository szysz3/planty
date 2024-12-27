package szysz3.planty.screen.taskdetails.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.taskdetails.viewmodel.TaskDetailsViewModel
import szysz3.planty.screen.tasklist.model.SubTask
import szysz3.planty.ui.widgets.RoundedButton

@Composable
fun TaskDetailsScreen(
    mainScreenViewModel: MainScreenViewModel,
    taskDetailsViewModel: TaskDetailsViewModel = hiltViewModel(),
    taskId: Int?,
    onNavigateBack: () -> Unit,
) {
    val uiState by taskDetailsViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        taskDetailsViewModel.loadTask(taskId)
    }

    val task = uiState.task ?: return // Exit early if task is null
    val activeSubTasks = task.tasks.filter { !it.isCompleted }
    val completedSubTasks = task.tasks.filter { it.isCompleted }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Task Title
        TextField(
            value = task.title,
            onValueChange = { taskDetailsViewModel.updateTaskTitle(it) },
            textStyle = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            placeholder = {
                Text(
                    text = "Title",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            modifier = Modifier.padding(16.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Active SubTasks
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            activeSubTasks.forEachIndexed { index, subTask ->
                SubTaskRow(
                    subTask = subTask,
                    onCheckedChange = { isChecked ->
                        taskDetailsViewModel.toggleSubTaskCompletion(index, isChecked)
                    },
                    onDescriptionChange = { newDescription ->
                        taskDetailsViewModel.updateSubTaskDescription(index, newDescription)
                    }
                )
            }

            // Add New SubTask
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    "+ New subtask",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    modifier = Modifier.clickable {
                        taskDetailsViewModel.addNewSubTask()
                    }
                )
            }

            // Divider
            if (completedSubTasks.isNotEmpty()) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Completed SubTasks
                completedSubTasks.forEach { subTask ->
                    SubTaskRow(subTask = subTask, enabled = false)
                }
            }
        }

        // Save Button
        RoundedButton(
            onClick = {
                taskDetailsViewModel.saveNewTask(task)
                onNavigateBack()
            },
            text = if (taskId == null) "Add" else "Update"
        )
    }
}

@Composable
fun SubTaskRow(
    subTask: SubTask,
    enabled: Boolean = true,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    onDescriptionChange: ((String) -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = subTask.isCompleted,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
        Spacer(modifier = Modifier.width(8.dp))
        TextField(
            value = subTask.description,
            onValueChange = { newText -> onDescriptionChange?.invoke(newText) },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                textDecoration = if (subTask.isCompleted) TextDecoration.LineThrough else null
            ), placeholder = {
                Text(
                    text = "Sub task title",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            modifier = Modifier.padding(16.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}
