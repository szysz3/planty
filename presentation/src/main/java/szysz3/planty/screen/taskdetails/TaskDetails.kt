package szysz3.planty.screen.taskdetails

import androidx.compose.animation.fadeIn
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import szysz3.planty.screen.taskdetails.TaskDetailsFeature.TASK_DETAILS_TASK_ID_ARG_NAME
import szysz3.planty.screen.taskdetails.screen.TaskDetailsScreen

object TaskDetailsFeature {
    const val TITLE = "Task Details"
    const val TASK_DETAILS_TASK_ID_ARG_NAME = "taskId"

    private const val ROUTE = "/taskList/taskDetails/{${TASK_DETAILS_TASK_ID_ARG_NAME}}"

    fun route() = ROUTE

    fun routeWithArgs(taskId: Long?): String {
        return ROUTE.replace(
            "{${TASK_DETAILS_TASK_ID_ARG_NAME}}",
            taskId.toString()
        )
    }
}

fun NavGraphBuilder.addTaskDetailsScreen(
    navController: NavHostController,
) {
    composable(
        enterTransition = {
            fadeIn()
        },
        exitTransition = {
            null
        },
        route = TaskDetailsFeature.route(),
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