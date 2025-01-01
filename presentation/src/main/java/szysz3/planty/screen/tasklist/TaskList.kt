package szysz3.planty.screen.tasklist

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import szysz3.planty.navigation.FeatureRoute
import szysz3.planty.navigation.staticComposable
import szysz3.planty.screen.tasklist.screen.TaskListScreen

object TaskListFeature : FeatureRoute {
    const val TITLE = "Task List"

    private const val BASE_ROUTE = "/taskList"

    override val basePath: String = BASE_ROUTE

    override val routeWithArgsPattern: String = basePath
}

fun NavGraphBuilder.addTaskListScreen(
    origin: String = "",
    navController: NavHostController,
    onShowTaskDetails: (Long) -> Unit,
    onAddNewTask: () -> Unit
) {
    staticComposable(
        route = TaskListFeature.route(origin),
    ) {
        TaskListScreen(
            title = TaskListFeature.TITLE,
            navController = navController,
            onShowTaskDetails = onShowTaskDetails,
            onAddNewTask = onAddNewTask
        )
    }
}