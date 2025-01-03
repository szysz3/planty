package szysz3.planty.screen.taskdetails.screen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import szysz3.planty.core.composable.DeleteAlertDialog
import szysz3.planty.screen.base.BaseScreen
import szysz3.planty.screen.base.topbar.TopBarBackButton
import szysz3.planty.screen.base.topbar.TopBarDeleteButton
import szysz3.planty.screen.taskdetails.composable.NewTaskButton
import szysz3.planty.screen.taskdetails.composable.SubTaskRow
import szysz3.planty.screen.taskdetails.composable.TaskFooter
import szysz3.planty.screen.taskdetails.composable.TaskTitle
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
            TaskTitle(
                title = uiState.task.title,
                onTitleChange = {
                    taskDetailsViewModel.updateTaskTitle(it)
                },
                focusRequester = focusRequester
            )

            DisposableEffect(Unit) {
                if (taskId == null) {
                    focusRequester.requestFocus()
                }
                onDispose { }
            }

            // task list
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // active tasks
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

                NewTaskButton { taskDetailsViewModel.addNewSubTask() }

                // completed tasks
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

            TaskFooter(state = uiState) {
                taskDetailsViewModel.saveNewTask()
                navController.popBackStack()
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