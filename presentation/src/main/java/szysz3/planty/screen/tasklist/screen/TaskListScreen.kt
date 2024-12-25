package szysz3.planty.screen.tasklist.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import szysz3.planty.R
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.tasklist.composable.TaskCard
import szysz3.planty.screen.tasklist.composable.TaskDetailsModal
import szysz3.planty.screen.tasklist.model.Task
import szysz3.planty.screen.tasklist.utils.dragContainer
import szysz3.planty.screen.tasklist.utils.draggableItems
import szysz3.planty.screen.tasklist.utils.rememberDragDropState
import szysz3.planty.screen.tasklist.viewmodel.TaskListViewModel
import szysz3.planty.ui.widgets.EllipticalBackground
import szysz3.planty.ui.widgets.FloatingActionButton

@Composable
fun TaskListScreen(
    mainScreenViewModel: MainScreenViewModel,
    taskListViewModel: TaskListViewModel = hiltViewModel()
) {
    val uiState by taskListViewModel.uiState.collectAsState()
    val tasks = uiState.tasks.sortedWith(
        compareBy<Task>(
            { it.tasks.none { subTask -> !subTask.isCompleted } },
            { it.tasks.all { subTask -> subTask.isCompleted } }
        )
    )
    val listState = rememberLazyListState()
    val dragDropState = rememberDragDropState(
        lazyListState = listState,
        draggableItemsNum = tasks.size,
        onMove = { fromIndex, toIndex ->
            taskListViewModel.moveTask(fromIndex, toIndex)
        }
    )
    var selectedTask by remember { mutableStateOf<Task?>(null) }

    // Background
    EllipticalBackground(R.drawable.bcg5)

    Box(modifier = Modifier.fillMaxSize()) {
        // Task List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .dragContainer(dragDropState),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            draggableItems(items = tasks, dragDropState = dragDropState) { modifier, task ->
                TaskCard(
                    task = task,
                    onSubTaskCheckedChanged = { task, subTask, isChecked ->
                        taskListViewModel.toggleSubTaskCompletion(
                            task,
                            subTask,
                            isChecked
                        )
                    },
                    onEditClicked = {
                        selectedTask = task // Open dialog with selected task
                    }
                )
            }
        }

        // FloatingActionButton in its exact form
        FloatingActionButton(
            icon = Icons.Rounded.Add,
            contentDescription = "Add task",
            onClick = {
                taskListViewModel.navigateToAddTaskScreen()
            }
        )

        // Task Details Modal with Animation
        TaskDetailsModal(
            task = selectedTask,
            onTaskUpdated = { updatedTask ->
                taskListViewModel.updateTask(updatedTask)
                selectedTask = updatedTask
            },
            onTaskDeleted = { taskToDelete ->
                taskListViewModel.deleteTask(taskToDelete)
                selectedTask = null
            },
            onDismiss = {
                selectedTask = null // Close modal
            },
            visible = selectedTask != null
        )
    }
}
