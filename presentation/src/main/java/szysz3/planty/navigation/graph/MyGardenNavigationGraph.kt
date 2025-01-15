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
            onPlantChosen = { plantId, row, column, gardenId ->
                navController.showScreen(
                    PlantDetailsFeature.routeWithArgs(
                        origin = MyGardenFeature.baseRoute(),
                        plantId = plantId,
                        row = row,
                        column = column,
                        config = PlantDetailsConfig.DELETE.value,
                        gardenId = gardenId
                    )
                )
            },
            onEmptyGardenFieldChosen = { row, column, gardenId ->
                navController.showScreen(
                    PlantCatalogFeature.routeWithArgs(
                        origin = MyGardenFeature.baseRoute(),
                        row = row,
                        column = column,
                        config = PlantDetailsConfig.PLANT.value,
                        gardenId = gardenId
                    )
                )
            }
        )

        addPlantDetailsScreen(
            navController = navController,
            onPlantImageClicked = { plantId ->
                navController.showScreen(
                    ImageGalleryFeature.routeWithArgs(
                        origin = "${MyGardenFeature.baseRoute()}${PlantDetailsFeature.baseRoute()}",
                        plantId = plantId
                    )
                )
            },
            origin = MyGardenFeature.baseRoute()
        )

        addPlantDetailsScreen(
            navController = navController,
            onPlantChosen = {
                navController.popBackStack(MyGardenFeature.route(), false)
            },
            onPlantImageClicked = { plantId ->
                navController.showScreen(
                    ImageGalleryFeature.routeWithArgs(
                        origin = "${MyGardenFeature.baseRoute()}${PlantCatalogFeature.baseRoute()}${PlantDetailsFeature.baseRoute()}",
                        plantId = plantId
                    )
                )
            },
            origin = "${MyGardenFeature.baseRoute()}${PlantCatalogFeature.baseRoute()}"
        )

        addPlantCatalogScreen(
            navController = navController,
            onShowPlantDetails = { plantId, row, column, gardenId ->
                navController.showScreen(
                    PlantDetailsFeature.routeWithArgs(
                        origin = "${MyGardenFeature.baseRoute()}${PlantCatalogFeature.baseRoute()}",
                        plantId = plantId,
                        row = row,
                        column = column,
                        config = PlantDetailsConfig.PLANT.value,
                        gardenId = gardenId
                    )
                )
            },
            origin = MyGardenFeature.baseRoute()
        )

        addImageGalleryScreen(
            navController = navController,
            origin = "${MyGardenFeature.baseRoute()}${PlantDetailsFeature.baseRoute()}"
        )

        addImageGalleryScreen(
            navController = navController,
            origin = "${MyGardenFeature.baseRoute()}${PlantCatalogFeature.baseRoute()}${PlantDetailsFeature.baseRoute()}"
        )
    }
}
