package szysz3.planty.screen.taskdetails.screen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import szysz3.planty.R
import szysz3.planty.core.composable.DeleteAlertDialog
import szysz3.planty.screen.base.BaseScreen
import szysz3.planty.screen.base.topbar.TopBarActionButton
import szysz3.planty.screen.base.topbar.TopBarBackButton
import szysz3.planty.screen.taskdetails.composable.NewTaskButton
import szysz3.planty.screen.taskdetails.composable.SubTaskRow
import szysz3.planty.screen.taskdetails.composable.TaskFooter
import szysz3.planty.screen.taskdetails.composable.TaskTitle
import szysz3.planty.screen.taskdetails.model.TaskDetailsScreenState
import szysz3.planty.screen.taskdetails.viewmodel.TaskDetailsViewModel
import szysz3.planty.theme.dimensions

@Composable
fun TaskDetailsScreen(
    title: String,
    navController: NavHostController,
    taskId: Long?,
    viewModel: TaskDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val isSystemInDarkTheme = isSystemInDarkTheme()

    LaunchedEffect(taskId) {
        viewModel.loadTask(taskId)
        viewModel.updateTheme(isSystemInDarkTheme)
    }

    BaseScreen(
        title = title,
        showTopBar = true,
        showBottomBar = true,
        topBarActions = {
            if (taskId != null) {
                TopBarActionButton(showButton = true) { viewModel.showDeleteDialog(true) }
            }
        },
        topBarBackNavigation = {
            TopBarBackButton(showBackButton = true, onBackClick = { navController.popBackStack() })
        },
        navController = navController
    ) { padding ->
        TaskContent(
            uiState = uiState,
            padding = padding,
            taskId = taskId,
            focusRequester = focusRequester,
            viewModel = viewModel,
            onNavigateBack = { navController.popBackStack() }
        )
    }
}

@Composable
private fun TaskContent(
    uiState: TaskDetailsScreenState,
    padding: PaddingValues,
    taskId: Long?,
    focusRequester: FocusRequester,
    viewModel: TaskDetailsViewModel,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        TaskTitle(
            title = uiState.task.title,
            onTitleChange = { viewModel.updateTaskTitle(it) },
            focusRequester = focusRequester
        )

        TaskList(
            modifier = Modifier.weight(1f),
            uiState = uiState,
            taskId = taskId,
            focusRequester = focusRequester,
            viewModel = viewModel
        )

        TaskFooter(
            state = uiState,
            onButtonClick = {
                viewModel.saveNewTask()
                onNavigateBack()
            }
        )

        if (uiState.isDeleteDialogVisible) {
            DeleteAlertDialog(
                title = stringResource(R.string.delete_task),
                message = stringResource(R.string.delete_task_confirmation),
                confirmButtonText = stringResource(R.string.delete_button),
                dismissButtonText = stringResource(R.string.cancel_button),
                onConfirmDelete = {
                    viewModel.deleteTask()
                    viewModel.showDeleteDialog(false)
                    onNavigateBack()
                },
                onCancel = { viewModel.showDeleteDialog(false) }
            )
        }
    }
}

@Composable
private fun TaskList(
    modifier: Modifier = Modifier,
    uiState: TaskDetailsScreenState,
    taskId: Long?,
    focusRequester: FocusRequester,
    viewModel: TaskDetailsViewModel
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        // Active tasks
        uiState.activeSubTasks.forEach { subTask ->
            SubTaskRow(
                modifier = Modifier.focusRequester(focusRequester),
                subTask = subTask,
                onCheckedChange = { viewModel.toggleSubTaskCompletion(subTask.id, it) },
                onDescriptionChange = { viewModel.updateSubTaskDescription(subTask.id, it) },
                onCostChange = { it?.let { cost -> viewModel.updateSubTaskCost(subTask.id, cost) } }
            )

            if (taskId == null) {
                DisposableEffect(Unit) {
                    focusRequester.requestFocus()
                    onDispose { }
                }
            }
        }

        NewTaskButton { viewModel.addNewSubTask() }

        // Completed tasks
        if (uiState.completedSubTasks.isNotEmpty()) {
            Divider(
                modifier = Modifier.padding(vertical = MaterialTheme.dimensions().spacing8),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )

            uiState.completedSubTasks.forEach { subTask ->
                SubTaskRow(subTask = subTask)
            }
        }
    }
}