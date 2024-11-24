package szysz3.planty.screen.tasklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.tasklist.model.Task
import szysz3.planty.screen.tasklist.utils.dragContainer
import szysz3.planty.screen.tasklist.utils.draggableItems
import szysz3.planty.screen.tasklist.utils.rememberDragDropState
import szysz3.planty.screen.tasklist.viewmodel.TaskListViewModel

@Composable
fun TaskListScreen(
    mainScreenViewModel: MainScreenViewModel,
    taskListViewModel: TaskListViewModel = hiltViewModel()
) {
    val tasks by taskListViewModel.tasks.collectAsState()
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
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            task.tasks.forEach { subTask ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = subTask.isCompleted,
                        onCheckedChange = { /* Handle state */ }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = subTask.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (subTask.isCompleted) {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }
        }
    }
}


