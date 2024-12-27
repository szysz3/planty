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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.taskdetails.viewmodel.TaskDetailsViewModel
import szysz3.planty.screen.tasklist.model.SubTask

@Composable
fun TaskDetailsScreen(
    mainScreenViewModel: MainScreenViewModel,
    taskDetailsViewModel: TaskDetailsViewModel = hiltViewModel()
) {
    val uiState by taskDetailsViewModel.uiState.collectAsState()
    val tasks = uiState.tasks

    if (tasks.isEmpty()) {
        Text("No tasks available.", modifier = Modifier.padding(16.dp))
        return
    }

    val selectedTask = tasks.first() // Replace with logic to select a specific task if needed
    var taskTitle by remember { mutableStateOf(selectedTask.title) }
    val activeSubTasks = selectedTask.tasks.filter { !it.isCompleted }
    val completedSubTasks = selectedTask.tasks.filter { it.isCompleted }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Editable Task Title
        BasicTextField(
            value = taskTitle,
            onValueChange = {
                taskTitle = it
                taskDetailsViewModel.updateTaskTitle(selectedTask.id, it)
            },
            textStyle = TextStyle(fontSize = 20.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Active SubTasks
        Column(modifier = Modifier.weight(1f)) {
            activeSubTasks.forEachIndexed { index, subTask ->
                SubTaskRow(
                    subTask = subTask,
                    onCheckedChange = { isChecked ->
                        taskDetailsViewModel.toggleSubTaskCompletion(
                            selectedTask.id,
                            index,
                            isChecked
                        )
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
                Text("+ New subtask", color = Color.Gray, modifier = Modifier.clickable {
                    taskDetailsViewModel.addNewSubTask(selectedTask.id)
                })
            }
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
}

@Composable
fun SubTaskRow(
    subTask: SubTask,
    enabled: Boolean = true,
    onCheckedChange: ((Boolean) -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = subTask.isCompleted,
            onCheckedChange = if (enabled) onCheckedChange else null,
            enabled = enabled
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(subTask.description)
    }
}
