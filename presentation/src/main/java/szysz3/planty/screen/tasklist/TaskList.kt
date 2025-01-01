package szysz3.planty.screen.tasklist

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import szysz3.planty.navigation.staticComposable
import szysz3.planty.screen.tasklist.screen.TaskListScreen

object TaskListFeature {
    const val TITLE = "Task List"
    private const val BASE_ROUTE = "/taskList"

    fun route(origin: String = "") = "$origin$BASE_ROUTE"
}

fun NavGraphBuilder.addTaskListScreen(
    origin: String = "",
    navController: NavHostController,
    onShowTaskDetails: (Long) -> Unit,
    onAddNewTask: () -> Unit
) {
    staticComposable(
        route = TaskListFeature.route(origin = origin),
    ) {
        TaskListScreen(
            title = TaskListFeature.TITLE,
            navController = navController,
            onShowTaskDetails = onShowTaskDetails,
            onAddNewTask = onAddNewTask
        )
    }
}