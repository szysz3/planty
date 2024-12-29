package szysz3.planty.screen.tasklist.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import szysz3.planty.R
import szysz3.planty.screen.tasklist.composable.TaskCardView
import szysz3.planty.screen.tasklist.model.Task
import szysz3.planty.screen.tasklist.utils.dragContainer
import szysz3.planty.screen.tasklist.utils.draggableItems
import szysz3.planty.screen.tasklist.utils.rememberDragDropState
import szysz3.planty.screen.tasklist.viewmodel.TaskListViewModel
import szysz3.planty.ui.widgets.EllipticalBackground
import szysz3.planty.ui.widgets.FloatingActionButton

@Composable
fun TaskListScreen(
    taskListViewModel: TaskListViewModel = hiltViewModel(),
    onNavigateToTaskDetails: (Task?) -> Unit
) {
    val uiState by taskListViewModel.uiState.collectAsState()
    val tasks = uiState.tasks
    val listState = rememberLazyListState()
    val dragDropState = rememberDragDropState(
        lazyListState = listState,
        draggableItemsNum = tasks.size,
        onMove = { fromIndex, toIndex ->
            taskListViewModel.moveTask(fromIndex, toIndex)
        },
        onDragEnd = { taskListViewModel.onPersistTaskOrder() }
    )

    LaunchedEffect(Unit) {
        taskListViewModel.observeTasks()
    }

    EllipticalBackground(R.drawable.bcg5)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .dragContainer(dragDropState),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            draggableItems(items = tasks, dragDropState = dragDropState) { modifier, task ->
                TaskCardView(
                    task = task,
                    modifier = modifier,
                    onClicked = { task -> onNavigateToTaskDetails(task) }
                )
            }
        }

        FloatingActionButton(
            icon = Icons.Rounded.Add,
            contentDescription = "Add task",
            onClick = {
                onNavigateToTaskDetails(null)
            })
    }
}