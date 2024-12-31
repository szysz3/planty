package szysz3.planty.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import szysz3.planty.core.model.PlantCatalogScreenOrigin
import szysz3.planty.core.model.PlantDetailsScreenOrigin
import szysz3.planty.navigation.bottombar.BottomBarNavigationItems
import szysz3.planty.screen.imagegallery.ImageGalleryFeature
import szysz3.planty.screen.imagegallery.addImageGalleryScreen
import szysz3.planty.screen.mygarden.MyGardenFeature
import szysz3.planty.screen.mygarden.addMyGardenScreen
import szysz3.planty.screen.plantcatalog.PlantCatalogFeature
import szysz3.planty.screen.plantcatalog.addPlantCatalogScreen
import szysz3.planty.screen.plantdetails.PlantDetailsFeature
import szysz3.planty.screen.plantdetails.addPlantDetailsScreen
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
        startDestination = BottomBarNavigationItems.MyGarden.route,
        modifier = modifier
    ) {
        addMyGardenScreen(navController,
            onPlantChosen = { plantId, row, column ->
                navController.showScreen(
                    PlantDetailsFeature.routeWithArgs(
                        plantId = plantId,
                        row = row,
                        column = column,
                        origin = PlantDetailsScreenOrigin.MY_GARDEN.value
                    )
                )
            },
            onEmptyGardenFieldChosen = { row, column ->
                navController.showScreen(
                    PlantCatalogFeature.routeWithArgs(
                        row = row,
                        column = column,
                        origin = PlantCatalogScreenOrigin.MY_GARDEN.value
                    )
                )
            })
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
                navController.popBackStack(MyGardenFeature.route(), false)
            },
            onPlantImageClicked = { plantId ->
                navController.showScreen(
                    ImageGalleryFeature.routeWithArgs(
                        plantId = plantId
                    )
                )
            }
        )
        addPlantCatalogScreen(
            navController,
            onShowPlantDetails = { origin, plantId, row, column ->
                val plantDetailsOrigin =
                    PlantDetailsScreenOrigin.fromPlantCatalogScreenOrigin(origin)
                navController.showScreen(
                    PlantDetailsFeature.routeWithArgs(
                        origin = plantDetailsOrigin.value,
                        plantId = plantId,
                        row = row,
                        column = column
                    )
                )

            }
        )
        addImageGalleryScreen(navController)
    }
}