package szysz3.planty.screen.taskdetails.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import szysz3.planty.core.composable.DeleteAlertDialog
import szysz3.planty.core.composable.RoundedButton
import szysz3.planty.screen.base.BaseScreen
import szysz3.planty.screen.base.topbar.TopBarBackButton
import szysz3.planty.screen.base.topbar.TopBarDeleteButton
import szysz3.planty.screen.taskdetails.composable.SubTaskRow
import szysz3.planty.screen.taskdetails.viewmodel.TaskDetailsViewModel

@Composable
fun TaskDetailsScreen(
    title: String,
    navController: NavHostController,
    taskId: Long?,
    taskDetailsViewModel: TaskDetailsViewModel = hiltViewModel(),
) {
    val uiState by taskDetailsViewModel.uiState.collectAsState()
    val isDarkMode = isSystemInDarkTheme()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isDarkMode) {
        taskDetailsViewModel.updateTheme(isDarkMode)
    }

    LaunchedEffect(taskId) {
        taskDetailsViewModel.loadTask(taskId)
    }

    BaseScreen(
        title = title,
        showTopBar = true,
        showBottomBar = true,
        topBarActions = {
            TopBarDeleteButton(
                showDeleteButton = taskId != null,
                onDeleteClick = {
                    taskDetailsViewModel.showDeleteDialog(true)
                }
            )
        },
        topBarBackNavigation = {
            TopBarBackButton(
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        },
        navController = navController
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Task Title
            TextField(
                value = uiState.task.title,
                onValueChange = { taskDetailsViewModel.updateTaskTitle(it) },
                textStyle = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                placeholder = {
                    Text(
                        text = "Title",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                modifier = Modifier
                    .padding(16.dp)
                    .focusRequester(focusRequester),
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

            DisposableEffect(Unit) {
                if (taskId == null) {
                    focusRequester.requestFocus()
                }
                onDispose { }
            }

            // Active SubTasks
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                uiState.activeSubTasks.forEach { subTask ->
                    SubTaskRow(
                        modifier = Modifier.focusRequester(focusRequester),
                        subTask = subTask,
                        onCheckedChange = { isChecked ->
                            taskDetailsViewModel.toggleSubTaskCompletion(subTask.id, isChecked)
                        },
                        onDescriptionChange = { newDescription ->
                            taskDetailsViewModel.updateSubTaskDescription(
                                subTask.id,
                                newDescription
                            )
                        },
                        onCostChange = { cost ->
                            cost?.let {
                                taskDetailsViewModel.updateSubTaskCost(subTask.id, it)
                            }
                        }
                    )

                    DisposableEffect(Unit) {
                        if (taskId == null) {
                            focusRequester.requestFocus()
                        }
                        onDispose { }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(
                        "+ New subtask",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        modifier = Modifier.clickable {
                            taskDetailsViewModel.addNewSubTask()
                        }
                    )
                }

                if (uiState.completedSubTasks.isNotEmpty()) {
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )

                    uiState.completedSubTasks.forEach { subTask ->
                        SubTaskRow(
                            subTask = subTask,
                        )
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                RoundedButton(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .align(Alignment.Bottom),
                    
                    onClick = {
                        taskDetailsViewModel.saveNewTask()
                        navController.popBackStack()
                    },
                    text = if (taskId == null) "Add" else "Update"
                )

                Spacer(modifier = Modifier.weight(1f))

                if (uiState.totalCost > 0) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .padding(end = 16.dp, bottom = 16.dp)
                    ) {
                        Text(
                            text = String.format(
                                "%.2f", uiState.completedSubTaskCost
                            ),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = " out of ",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = String.format(
                                "%.2f",
                                uiState.totalCost
                            ),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }

        if (uiState.isDeleteDialogVisible) {
            DeleteAlertDialog(
                title = "Delete Task",
                message = "Are you sure you want to delete this task?",
                confirmButtonText = "Delete",
                dismissButtonText = "Cancel",
                onConfirmDelete = {
                    taskDetailsViewModel.deleteTask()
                    taskDetailsViewModel.showDeleteDialog(false)
                    navController.popBackStack()
                },
                onCancel = {
                    taskDetailsViewModel.showDeleteDialog(false)
                }
            )
        }
    }
}