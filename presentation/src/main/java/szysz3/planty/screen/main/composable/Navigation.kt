package szysz3.planty.screen.main.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import szysz3.planty.navigation.BottomBarNavigation
import szysz3.planty.navigation.showScreen
import szysz3.planty.screen.imagegallery.screen.ImageGalleryScreen
import szysz3.planty.screen.mygarden.addMyGardenScreen
import szysz3.planty.screen.plantcatalog.addPlantCatalogScreen
import szysz3.planty.screen.plantdetails.PlantDetailsFeature
import szysz3.planty.screen.plantdetails.addPlantDetailsScreen
import szysz3.planty.screen.plantdetails.model.PlantDetailsScreenOrigin
import szysz3.planty.screen.plantid.addPlantIdScreen
import szysz3.planty.screen.taskdetails.TaskDetailsFeature
import szysz3.planty.screen.taskdetails.addTaskDetailsScreen
import szysz3.planty.screen.tasklist.addTaskListScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarNavigation.MyGarden.route,
        modifier = modifier
    ) {
        addMyGardenScreen(navController)
        addTaskListScreen(
            navController,
            onShowTaskDetails = { taskId ->
                navController.showScreen(TaskDetailsFeature.routeWithArgs(taskId))
            },
            onAddNewTask = {
                navController.showScreen(TaskDetailsFeature.routeWithArgs(null))
            }
        )
        addTaskDetailsScreen(navController)
        addPlantIdScreen(navController, onShowPlantDetails = { plantId ->
            navController.showScreen(
                PlantDetailsFeature.routeWithArgs(
                    origin = PlantDetailsScreenOrigin.PLANT_ID_SCREEN.value,
                    plantId = plantId
                )
            )

        })
        addPlantDetailsScreen(
            navController,
            onPlantChosen = {
                navController.popBackStack()
            },
            onPlantImageClicked = { plantId ->
//                    navigate(navController, NavigationItem.ImageGallery.withArgs(plantId))
            }
        )

        addPlantCatalogScreen(
            navController,
            onShowPlantDetails = { origin, plantId ->
                navController.showScreen(
                    PlantDetailsFeature.routeWithArgs(
                        origin = origin.value,
                        plantId = plantId
                    )
                )

            }
        )

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
    }
}

open class NavigationItem(val route: String, val title: String) {

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
        const val IMAGE_GALLERY_PLANT_ID_ARG_NAME = "plantId"
    }
}
