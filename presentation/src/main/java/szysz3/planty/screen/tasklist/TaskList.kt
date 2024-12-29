package szysz3.planty.screen.tasklist

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import szysz3.planty.screen.tasklist.screen.TaskListScreen

object TaskListFeature {
    const val TITLE = "Task List"
    private const val ROUTE = "taskList"

    fun route() = ROUTE
}

fun NavGraphBuilder.addTaskListScreen(
    navController: NavHostController,
    onShowTaskDetails: (Long) -> Unit,
    onAddNewTask: () -> Unit
) {
    composable(TaskListFeature.route()) {
        TaskListScreen(
            title = TaskListFeature.TITLE,
            navController = navController,
            onShowTaskDetails = onShowTaskDetails,
            onAddNewTask = onAddNewTask
        )
    }
}