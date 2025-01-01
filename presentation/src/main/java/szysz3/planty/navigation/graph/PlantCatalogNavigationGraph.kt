package szysz3.planty.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import szysz3.planty.navigation.bottombar.BottomBarNavigationItems
import szysz3.planty.navigation.showScreen
import szysz3.planty.screen.imagegallery.ImageGalleryFeature
import szysz3.planty.screen.imagegallery.addImageGalleryScreen
import szysz3.planty.screen.plantcatalog.PlantCatalogFeature
import szysz3.planty.screen.plantcatalog.addPlantCatalogScreen
import szysz3.planty.screen.plantdetails.PlantDetailsFeature
import szysz3.planty.screen.plantdetails.addPlantDetailsScreen

fun NavGraphBuilder.plantCatalogGraph(navController: NavHostController) {
    navigation(
        startDestination = PlantCatalogFeature.route(),
        route = BottomBarNavigationItems.PlantCatalog.route
    ) {
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

        addImageGalleryScreen(
            navController = navController,
            origin = "${PlantCatalogFeature.route()}${PlantDetailsFeature.route()}"
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
    }
}