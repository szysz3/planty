package szysz3.planty.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import szysz3.planty.navigation.bottombar.BottomBarNavigationItems
import szysz3.planty.navigation.showScreen
import szysz3.planty.screen.taskdetails.TaskDetailsFeature
import szysz3.planty.screen.taskdetails.addTaskDetailsScreen
import szysz3.planty.screen.tasklist.TaskListFeature
import szysz3.planty.screen.tasklist.addTaskListScreen

fun NavGraphBuilder.taskListGraph(navController: NavHostController) {
    navigation(
        startDestination = TaskListFeature.route(),
        route = BottomBarNavigationItems.TaskList.route
    ) {
        addTaskListScreen(
            navController = navController,
            onShowTaskDetails = { taskId ->
                navController.showScreen(
                    TaskDetailsFeature.routeWithArgs(
                        origin = TaskListFeature.baseRoute(),
                        taskId = taskId
                    )
                )
            },
            onAddNewTask = {
                navController.showScreen(
                    TaskDetailsFeature.routeWithArgs(
                        origin = TaskListFeature.baseRoute(),
                        taskId = null
                    )
                )
            }
        )

        addTaskDetailsScreen(
            navController = navController,
            origin = TaskListFeature.baseRoute()
        )
    }
}
