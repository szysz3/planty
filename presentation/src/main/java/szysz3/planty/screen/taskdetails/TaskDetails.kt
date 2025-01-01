package szysz3.planty.screen.taskdetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import szysz3.planty.navigation.FeatureRoute
import szysz3.planty.navigation.buildQueryString
import szysz3.planty.navigation.staticComposable
import szysz3.planty.screen.taskdetails.TaskDetailsFeature.TASK_DETAILS_TASK_ID_ARG_NAME
import szysz3.planty.screen.taskdetails.screen.TaskDetailsScreen

object TaskDetailsFeature : FeatureRoute {
    const val TITLE = "Task Details"
    const val TASK_DETAILS_TASK_ID_ARG_NAME = "taskId"

    private const val BASE_ROUTE = "/taskDetails"

    override val basePath: String = BASE_ROUTE

    override val routeWithArgsPattern: String =
        "$basePath?$TASK_DETAILS_TASK_ID_ARG_NAME={$TASK_DETAILS_TASK_ID_ARG_NAME}"

    fun routeWithArgs(origin: String = "", taskId: Long?): String {
        val queryParams = buildQueryString(
            TASK_DETAILS_TASK_ID_ARG_NAME to taskId
        )
        return baseRoute(origin) + queryParams
    }
}

fun NavGraphBuilder.addTaskDetailsScreen(
    origin: String = "",
    navController: NavHostController,
) {
    staticComposable(
        route = TaskDetailsFeature.route(origin),
        arguments = listOf(
            navArgument(TASK_DETAILS_TASK_ID_ARG_NAME) {
                type = NavType.StringType
                nullable = true
            }
        )
    ) { backStackEntry ->
        val taskId = backStackEntry.arguments
            ?.getString(TASK_DETAILS_TASK_ID_ARG_NAME)
            ?.toLongOrNull()

        TaskDetailsScreen(
            title = TaskDetailsFeature.TITLE,
            navController = navController,
            taskId = taskId
        )
    }
}