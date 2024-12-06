package szysz3.planty.screen.tasklist.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import szysz3.planty.R
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.tasklist.composable.TaskCard
import szysz3.planty.screen.tasklist.utils.dragContainer
import szysz3.planty.screen.tasklist.utils.draggableItems
import szysz3.planty.screen.tasklist.utils.rememberDragDropState
import szysz3.planty.screen.tasklist.viewmodel.TaskListViewModel
import szysz3.planty.ui.widgets.EllipticalBackground

@Composable
fun TaskListScreen(
    mainScreenViewModel: MainScreenViewModel,
    taskListViewModel: TaskListViewModel = hiltViewModel()
) {
    val uiState by taskListViewModel.uiState.collectAsState()
    val tasks = uiState.tasks
    val listState = rememberLazyListState()
    val dragDropState = rememberDragDropState(
        lazyListState = listState,
        draggableItemsNum = tasks.size,
        onMove = { fromIndex, toIndex ->
            taskListViewModel.moveTask(fromIndex, toIndex)
        }
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd // For the Floating Action Button
    ) {
        EllipticalBackground(R.drawable.task_screen_bcg)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .dragContainer(dragDropState)
                .padding(16.dp), // Adjust padding as needed
            state = listState,
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            draggableItems(items = tasks, dragDropState = dragDropState) { modifier, task ->
                TaskCard(
                    task = task,
                    modifier = modifier,
                    onTaskCheckedChanged = { task, isChecked ->
                        taskListViewModel.toggleTaskCompletion(task, isChecked)
                    }
                )
            }
        }

        FloatingActionButton(
            onClick = { taskListViewModel.navigateToAddTaskScreen() },
            modifier = Modifier
                .padding(16.dp),
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Add Task"
            )
        }
    }
}