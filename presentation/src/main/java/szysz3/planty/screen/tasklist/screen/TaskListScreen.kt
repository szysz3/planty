package szysz3.planty.screen.tasklist.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.tasklist.composable.TaskCard
import szysz3.planty.screen.tasklist.utils.dragContainer
import szysz3.planty.screen.tasklist.utils.draggableItems
import szysz3.planty.screen.tasklist.utils.rememberDragDropState
import szysz3.planty.screen.tasklist.viewmodel.TaskListViewModel

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

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                taskListViewModel.navigateToAddTaskScreen()
            }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .dragContainer(dragDropState)
                .padding(paddingValues),
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
    }
}