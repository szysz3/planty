package szysz3.planty.screen.main.composable

import MyGardenScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import szysz3.planty.screen.imagegallery.screen.ImageGalleryScreen
import szysz3.planty.screen.plantcatalog.screen.PlantCatalogScreen
import szysz3.planty.screen.plantdetails.model.PlantDetailsScreenOrigin
import szysz3.planty.screen.plantdetails.screen.PlantDetailsScreen
import szysz3.planty.screen.plantid.screen.PlantIdScreen
import szysz3.planty.screen.taskdetails.screen.TaskDetailsScreen
import szysz3.planty.screen.tasklist.screen.TaskListScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Home.route) {
            MyGardenScreen(
                onNavigateToPlantAPlant = {
                    navigate(navController, BottomNavItem.Catalog)
                },
                onNavigateToPlantDetails = { origin, plantId ->
                    navigate(
                        navController,
                        NavigationItem.PlantDetails.withArgs(origin.value, plantId)
                    )
                }
            )
        }
        composable(BottomNavItem.TaskList.route) {
            TaskListScreen(
            ) { task ->
                navigate(navController, NavigationItem.TaskDetails.withArgs(task?.id?.toInt()))
            }
        }
        composable(BottomNavItem.Catalog.route) {
            PlantCatalogScreen(
            ) { origin, plantId ->
                navigate(navController, NavigationItem.PlantDetails.withArgs(origin.value, plantId))
            }
        }
        composable(BottomNavItem.PlantId.route) {
            PlantIdScreen() { localMatchingPlant ->
                localMatchingPlant?.let {
                    navigate(
                        navController,
                        NavigationItem.PlantDetails.withArgs(
                            PlantDetailsScreenOrigin.PLANT_ID_SCREEN.value,
                            localMatchingPlant.id
                        )
                    )
                }
            }
        }
        composable(
            route = NavigationItem.ImageGallery.route,
            arguments = listOf(
                navArgument(NavigationItem.IMAGE_GALLERY_PLANT_ID_ARG_NAME) {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val plantId =
                backStackEntry.arguments?.getInt(NavigationItem.IMAGE_GALLERY_PLANT_ID_ARG_NAME)
                    ?: -1
            ImageGalleryScreen(plantId = plantId) {
                // on close action
            }
        }

        composable(
            route = NavigationItem.PlantDetails.route,
            arguments = listOf(
                navArgument(NavigationItem.PLANT_DETAILS_ORIGIN_ARG_NAME) {
                    type = NavType.IntType
                    nullable = false
                },
                navArgument(NavigationItem.PLANT_DETAILS_PLANT_ID_ARG_NAME) {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val origin =
                backStackEntry.arguments?.getInt(NavigationItem.PLANT_DETAILS_ORIGIN_ARG_NAME) ?: -1
            val plantId =
                backStackEntry.arguments?.getInt(NavigationItem.PLANT_DETAILS_PLANT_ID_ARG_NAME)
                    ?: -1
            PlantDetailsScreen(
                origin = PlantDetailsScreenOrigin.fromValue(origin),
                onPlantChosen = {
                    navController.popBackStack(BottomNavItem.Home.route, false)
                },
                plantId = plantId,
                onPlantImageClicked = { plantId ->
                    navigate(navController, NavigationItem.ImageGallery.withArgs(plantId))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = NavigationItem.TaskDetails.route,
            arguments = listOf(
                navArgument(NavigationItem.TASK_DETAILS_TASK_ID_ARG_NAME) {
                    type = NavType.StringType
                    nullable = true
                }
            )) { backStackEntry ->
            val taskId =
                backStackEntry.arguments?.getString(NavigationItem.TASK_DETAILS_TASK_ID_ARG_NAME)
                    ?.toIntOrNull()
            TaskDetailsScreen(
                taskId = taskId
            ) {
                navController.popBackStack()
            }
        }
    }
}

fun navigate(navController: NavHostController, navigationItem: NavigationItem) {
    navController.navigate(navigationItem.route) {
        launchSingleTop = true
        restoreState = true
    }
}

open class NavigationItem(val route: String, val title: String) {
    object TaskDetails :
        NavigationItem("/taskList/taskDetails/{${TASK_DETAILS_TASK_ID_ARG_NAME}}", "Task details") {
        fun withArgs(taskId: Int?): NavigationItem {
            return NavigationItem(
                route.replace(
                    "{${TASK_DETAILS_TASK_ID_ARG_NAME}}",
                    taskId.toString()
                ),
                title
            )
        }
    }

    //    object PlantCatalog : NavigationItem("/myGarden/plantCatalog", "Plant catalog")
    object PlantDetails :
        NavigationItem(
            "/plantDetails/{${PLANT_DETAILS_ORIGIN_ARG_NAME}}/{${PLANT_DETAILS_PLANT_ID_ARG_NAME}}",
            "Plant details"
        ) {
        fun withArgs(origin: Int, plantId: Int): NavigationItem {
            return NavigationItem(
                route.replace(
                    "{${PLANT_DETAILS_ORIGIN_ARG_NAME}}",
                    origin.toString()
                ).replace(
                    "{${PLANT_DETAILS_PLANT_ID_ARG_NAME}}",
                    plantId.toString()
                ),

                title
            )
        }
    }

    object ImageGallery :
        NavigationItem(
            "/imageGallery/{${IMAGE_GALLERY_PLANT_ID_ARG_NAME}}",
            "Image gallery"
        ) {
        fun withArgs(plantId: Int): NavigationItem {
            return NavigationItem(
                route.replace(
                    "{${IMAGE_GALLERY_PLANT_ID_ARG_NAME}}",
                    plantId.toString()
                ),
                title
            )
        }
    }

    companion object {
        const val PLANT_DETAILS_ORIGIN_ARG_NAME = "origin"
        const val PLANT_DETAILS_PLANT_ID_ARG_NAME = "plantId"
        const val IMAGE_GALLERY_PLANT_ID_ARG_NAME = "plantId"
        const val TASK_DETAILS_TASK_ID_ARG_NAME = "taskId"

        fun getTitleForRoute(route: String): String? {
            return when {
                route.startsWith(PlantDetails.route.substringBefore("/{")) -> PlantDetails.title
                route == BottomNavItem.Catalog.route -> BottomNavItem.Catalog.title
                route == BottomNavItem.Home.route -> BottomNavItem.Home.title
                route == BottomNavItem.TaskList.route -> BottomNavItem.TaskList.title
                route == BottomNavItem.PlantId.route -> BottomNavItem.PlantId.title
                else -> null
            }
        }
    }
}

open class BottomNavItem(route: String, title: String, val icon: ImageVector) :
    NavigationItem(route, title) {
    object Home : BottomNavItem("myGarden", "My Garden", Icons.Rounded.Home)
    object TaskList : BottomNavItem("taskList", "Tasks", Icons.Rounded.Done)
    object Catalog : BottomNavItem("catalog", "Catalog", Icons.Rounded.Info)
    object PlantId :
        BottomNavItem("plantId", "Id", Icons.Rounded.Search)
}

