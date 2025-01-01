package szysz3.planty.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import szysz3.planty.navigation.bottombar.BottomBarNavigationItems
import szysz3.planty.screen.imagegallery.ImageGalleryFeature
import szysz3.planty.screen.imagegallery.addImageGalleryScreen
import szysz3.planty.screen.mygarden.MyGardenFeature
import szysz3.planty.screen.mygarden.addMyGardenScreen
import szysz3.planty.screen.plantcatalog.PlantCatalogFeature
import szysz3.planty.screen.plantcatalog.addPlantCatalogScreen
import szysz3.planty.screen.plantdetails.PlantDetailsFeature
import szysz3.planty.screen.plantdetails.addPlantDetailsScreen
import szysz3.planty.screen.plantid.PlantIdFeature
import szysz3.planty.screen.plantid.addPlantIdScreen
import szysz3.planty.screen.taskdetails.TaskDetailsFeature
import szysz3.planty.screen.taskdetails.addTaskDetailsScreen
import szysz3.planty.screen.tasklist.TaskListFeature
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
        addMyGardenScreen(
            navController = navController,
            onPlantChosen = { plantId, row, column ->
                navController.showScreen(
                    PlantDetailsFeature.routeWithArgs(
                        plantId = plantId,
                        row = row,
                        column = column,
                        origin = MyGardenFeature.route()
                    )
                )
            },
            onEmptyGardenFieldChosen = { row, column ->
                navController.showScreen(
                    PlantCatalogFeature.routeWithArgs(
                        row = row,
                        column = column,
                        origin = MyGardenFeature.route()
                    )
                )
            })


        addTaskListScreen(
            navController = navController,
            onShowTaskDetails = { taskId ->
                navController.showScreen(
                    TaskDetailsFeature.routeWithArgs(
                        origin = TaskListFeature.route(),
                        taskId = taskId
                    )
                )
            },
            onAddNewTask = {
                navController.showScreen(
                    TaskDetailsFeature.routeWithArgs(
                        origin = TaskListFeature.route(),
                        taskId = null
                    )
                )
            }
        )

        addTaskDetailsScreen(
            navController = navController,
            origin = TaskListFeature.route()
        )

        addPlantIdScreen(navController = navController,
            onShowPlantDetails = { plantId ->
                navController.showScreen(
                    PlantDetailsFeature.routeWithArgs(
                        origin = PlantIdFeature.route(),
                        plantId = plantId
                    )
                )
            })

        addPlantDetailsScreen(
            navController = navController,
            onPlantImageClicked = { plantId ->
                navController.showScreen(
                    ImageGalleryFeature.routeWithArgs(
                        origin = "${MyGardenFeature.route()}${PlantDetailsFeature.route()}",
                        plantId = plantId
                    )
                )
            },
            origin = MyGardenFeature.route()
        )

        addPlantDetailsScreen(
            navController = navController,
            onPlantImageClicked = { plantId ->
                navController.showScreen(
                    ImageGalleryFeature.routeWithArgs(
                        origin = "${PlantCatalogFeature.route()}${PlantDetailsFeature.route()}",
                        plantId = plantId
                    )
                )
            },
            origin = PlantCatalogFeature.route()
        )

        addPlantDetailsScreen(
            navController = navController,
            onPlantImageClicked = { plantId ->
                navController.showScreen(
                    ImageGalleryFeature.routeWithArgs(
                        origin = "${PlantIdFeature.route()}${PlantDetailsFeature.route()}",
                        plantId = plantId
                    )
                )
            },
            origin = PlantIdFeature.route()
        )

        addPlantDetailsScreen(
            navController = navController,
            onPlantChosen = {
                navController.popBackStack(MyGardenFeature.route(), false)
            },
            onPlantImageClicked = { plantId ->
                navController.showScreen(
                    ImageGalleryFeature.routeWithArgs(
                        origin = "${MyGardenFeature.route()}${PlantCatalogFeature.route()}${PlantDetailsFeature.route()}",
                        plantId = plantId
                    )
                )
            },
            origin = "${MyGardenFeature.route()}${PlantCatalogFeature.route()}"
        )

        addPlantCatalogScreen(
            navController = navController,
            onShowPlantDetails = { origin, plantId, row, column ->
                navController.showScreen(
                    PlantDetailsFeature.routeWithArgs(
                        origin = PlantCatalogFeature.route(),
                        plantId = plantId,
                        row = row,
                        column = column
                    )
                )
            }
        )

        addPlantCatalogScreen(
            navController = navController,
            onShowPlantDetails = { origin, plantId, row, column ->
                navController.showScreen(
                    PlantDetailsFeature.routeWithArgs(
                        origin = "${MyGardenFeature.route()}${PlantCatalogFeature.route()}",
                        plantId = plantId,
                        row = row,
                        column = column
                    )
                )
            },
            origin = MyGardenFeature.route()
        )

        addImageGalleryScreen(
            navController = navController,
            origin = "${MyGardenFeature.route()}${PlantCatalogFeature.route()}${PlantDetailsFeature.route()}"
        )

        addImageGalleryScreen(
            navController = navController,
            origin = "${PlantCatalogFeature.route()}${PlantDetailsFeature.route()}"
        )

        addImageGalleryScreen(
            navController = navController,
            origin = "${MyGardenFeature.route()}${PlantDetailsFeature.route()}"
        )

        addImageGalleryScreen(
            navController = navController,
            origin = "${PlantIdFeature.route()}${PlantDetailsFeature.route()}"
        )
    }
}