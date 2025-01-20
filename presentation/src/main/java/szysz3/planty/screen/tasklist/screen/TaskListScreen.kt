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
import androidx.navigation.NavHostController
import szysz3.planty.R
import szysz3.planty.core.composable.EllipticalBackground
import szysz3.planty.core.composable.FloatingActionButton
import szysz3.planty.screen.base.BaseScreen
import szysz3.planty.screen.tasklist.composable.TaskCardView
import szysz3.planty.screen.tasklist.model.toTaskCardUiState
import szysz3.planty.screen.tasklist.utils.dragContainer
import szysz3.planty.screen.tasklist.utils.draggableItems
import szysz3.planty.screen.tasklist.utils.rememberDragDropState
import szysz3.planty.screen.tasklist.viewmodel.TaskListViewModel

@Composable
fun TaskListScreen(
    title: String,
    navController: NavHostController,
    onShowTaskDetails: (Long) -> Unit,
    onAddNewTask: () -> Unit,
    taskListViewModel: TaskListViewModel = hiltViewModel(),
) {
    val uiState by taskListViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val dragDropState = rememberDragDropState(
        lazyListState = listState,
        draggableItemsNum = uiState.tasks.size,
        onMove = { fromIndex, toIndex ->
            taskListViewModel.moveTask(fromIndex, toIndex)
        },
        onDragEnd = { taskListViewModel.onPersistTaskOrder() }
    )

    LaunchedEffect(Unit) {
        taskListViewModel.observeTasks()
    }

    BaseScreen(
        title = title,
        showTopBar = true,
        showBottomBar = true,
        navController = navController
    ) { padding ->

        EllipticalBackground(R.drawable.bcg5)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .dragContainer(dragDropState),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                draggableItems(
                    items = uiState.tasks,
                    dragDropState = dragDropState
                ) { modifier, task ->
                    TaskCardView(
                        uiState = task.toTaskCardUiState(),
                        modifier = modifier,
                        onClicked = {
                            onShowTaskDetails(task.id)
                        }
                    )
                }
            }

            FloatingActionButton(
                icon = Icons.Rounded.Add,
                contentDescription = "Add task",
                onClick = {
                    onAddNewTask()
                })
        }
    }
}