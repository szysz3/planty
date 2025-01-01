package szysz3.planty.screen.taskdetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import szysz3.planty.navigation.staticComposable
import szysz3.planty.screen.taskdetails.TaskDetailsFeature.TASK_DETAILS_TASK_ID_ARG_NAME
import szysz3.planty.screen.taskdetails.screen.TaskDetailsScreen

object TaskDetailsFeature {
    const val TITLE = "Task Details"
    const val TASK_DETAILS_TASK_ID_ARG_NAME = "taskId"

    private const val BASE_ROUTE = "/taskDetails"

    private const val BASE_ROUTE_WITH_ARGS =
        "$BASE_ROUTE?$TASK_DETAILS_TASK_ID_ARG_NAME={${TASK_DETAILS_TASK_ID_ARG_NAME}}"

    private fun baseRoute(origin: String = "") = "$origin${BASE_ROUTE}"

    fun route(origin: String = "") = "$origin${BASE_ROUTE_WITH_ARGS}"

    fun routeWithArgs(origin: String = "", taskId: Long?): String {
        val queryParams = buildList {
            add("$TASK_DETAILS_TASK_ID_ARG_NAME=$taskId")
        }

        val finalRoute = if (queryParams.isNotEmpty()) {
            "${baseRoute(origin)}?${queryParams.joinToString("&")}"
        } else {
            baseRoute(origin)
        }

        return finalRoute
    }
}

fun NavGraphBuilder.addTaskDetailsScreen(
    origin: String = "",
    navController: NavHostController,
) {
    staticComposable(
        route = TaskDetailsFeature.route(origin = origin),
        arguments = listOf(
            navArgument(TASK_DETAILS_TASK_ID_ARG_NAME) {
                type = NavType.StringType
                nullable = true
            }
        )) { backStackEntry ->

        val taskId =
            backStackEntry.arguments?.getString(TASK_DETAILS_TASK_ID_ARG_NAME)
                ?.toLongOrNull()

        TaskDetailsScreen(
            title = TaskDetailsFeature.TITLE,
            navController = navController,
            taskId = taskId
        )
    }
}