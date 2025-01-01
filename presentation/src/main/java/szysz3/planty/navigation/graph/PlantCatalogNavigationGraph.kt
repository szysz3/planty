package szysz3.planty.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import szysz3.planty.core.model.PlantDetailsConfig
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
        startDestination = PlantCatalogFeature.baseRoute(),
        route = BottomBarNavigationItems.PlantCatalog.route
    ) {
        addPlantCatalogScreen(
            navController = navController,
            onShowPlantDetails = { plantId, row, column ->
                navController.showScreen(
                    PlantDetailsFeature.routeWithArgs(
                        origin = PlantCatalogFeature.baseRoute(),
                        plantId = plantId,
                        row = row,
                        column = column,
                        config = PlantDetailsConfig.PREVIEW.value
                    )
                )
            }
        )

        addImageGalleryScreen(
            navController = navController,
            origin = "${PlantCatalogFeature.baseRoute()}${PlantDetailsFeature.baseRoute()}"
        )

        addPlantDetailsScreen(
            navController = navController,
            onPlantImageClicked = { plantId ->
                navController.showScreen(
                    ImageGalleryFeature.routeWithArgs(
                        origin = "${PlantCatalogFeature.baseRoute()}${PlantDetailsFeature.baseRoute()}",
                        plantId = plantId
                    )
                )
            },
            origin = PlantCatalogFeature.baseRoute()
        )
    }
}