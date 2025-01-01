package szysz3.planty.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import szysz3.planty.core.model.PlantDetailsConfig
import szysz3.planty.navigation.bottombar.BottomBarNavigationItems
import szysz3.planty.navigation.showScreen
import szysz3.planty.screen.imagegallery.ImageGalleryFeature
import szysz3.planty.screen.imagegallery.addImageGalleryScreen
import szysz3.planty.screen.mygarden.MyGardenFeature
import szysz3.planty.screen.mygarden.addMyGardenScreen
import szysz3.planty.screen.plantcatalog.PlantCatalogFeature
import szysz3.planty.screen.plantcatalog.addPlantCatalogScreen
import szysz3.planty.screen.plantdetails.PlantDetailsFeature
import szysz3.planty.screen.plantdetails.addPlantDetailsScreen

fun NavGraphBuilder.myGardenGraph(navController: NavHostController) {
    navigation(
        startDestination = MyGardenFeature.route(),
        route = BottomBarNavigationItems.MyGarden.route
    ) {
        addMyGardenScreen(
            navController = navController,
            onPlantChosen = { plantId, row, column ->
                navController.showScreen(
                    PlantDetailsFeature.routeWithArgs(
                        plantId = plantId,
                        row = row,
                        column = column,
                        origin = MyGardenFeature.route(),
                        config = PlantDetailsConfig.DELETE.value
                    )
                )
            },
            onEmptyGardenFieldChosen = { row, column ->
                navController.showScreen(
                    PlantCatalogFeature.routeWithArgs(
                        row = row,
                        column = column,
                        origin = MyGardenFeature.route(),
                        config = PlantDetailsConfig.PLANT.value
                    )
                )
            })

        addPlantDetailsScreen(
            navController = navController,
            onPlantImageClicked = { plantId ->
                navController.showScreen(
                    ImageGalleryFeature.routeWithArgs(
                        origin = "${MyGardenFeature.route()}${PlantDetailsFeature.baseRoute()}",
                        plantId = plantId
                    )
                )
            },
            origin = MyGardenFeature.route()
        )

        addPlantDetailsScreen(
            navController = navController,
            onPlantChosen = {
                navController.popBackStack(MyGardenFeature.route(), false)
            },
            onPlantImageClicked = { plantId ->
                navController.showScreen(
                    ImageGalleryFeature.routeWithArgs(
                        origin = "${MyGardenFeature.route()}${PlantCatalogFeature.baseRoute()}${PlantDetailsFeature.baseRoute()}",
                        plantId = plantId
                    )
                )
            },
            origin = "${MyGardenFeature.route()}${PlantCatalogFeature.baseRoute()}"
        )

        addPlantCatalogScreen(
            navController = navController,
            onShowPlantDetails = { plantId, row, column ->
                navController.showScreen(
                    PlantDetailsFeature.routeWithArgs(
                        origin = "${MyGardenFeature.route()}${PlantCatalogFeature.baseRoute()}",
                        plantId = plantId,
                        row = row,
                        column = column,
                        config = PlantDetailsConfig.PLANT.value
                    )
                )
            },
            origin = MyGardenFeature.route()
        )

        addImageGalleryScreen(
            navController = navController,
            origin = "${MyGardenFeature.route()}${PlantDetailsFeature.baseRoute()}"
        )

        addImageGalleryScreen(
            navController = navController,
            origin = "${MyGardenFeature.route()}${PlantCatalogFeature.baseRoute()}${PlantDetailsFeature.baseRoute()}"
        )
    }
}