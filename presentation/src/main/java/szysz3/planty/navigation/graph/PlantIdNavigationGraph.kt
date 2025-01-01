package szysz3.planty.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import szysz3.planty.core.model.PlantDetailsConfig
import szysz3.planty.navigation.bottombar.BottomBarNavigationItems
import szysz3.planty.navigation.showScreen
import szysz3.planty.screen.imagegallery.ImageGalleryFeature
import szysz3.planty.screen.imagegallery.addImageGalleryScreen
import szysz3.planty.screen.plantdetails.PlantDetailsFeature
import szysz3.planty.screen.plantdetails.addPlantDetailsScreen
import szysz3.planty.screen.plantid.PlantIdFeature
import szysz3.planty.screen.plantid.addPlantIdScreen

fun NavGraphBuilder.plantIdGraph(navController: NavHostController) {
    navigation(
        startDestination = PlantIdFeature.route(),
        route = BottomBarNavigationItems.PlantId.route
    ) {
        addPlantIdScreen(navController = navController,
            onShowPlantDetails = { plantId ->
                navController.showScreen(
                    PlantDetailsFeature.routeWithArgs(
                        origin = PlantIdFeature.route(),
                        plantId = plantId,
                        config = PlantDetailsConfig.PREVIEW.value
                    )
                )
            })

        addPlantDetailsScreen(
            navController = navController,
            onPlantImageClicked = { plantId ->
                navController.showScreen(
                    ImageGalleryFeature.routeWithArgs(
                        origin = "${PlantIdFeature.route()}${PlantDetailsFeature.baseRoute()}",
                        plantId = plantId
                    )
                )
            },
            origin = PlantIdFeature.route()
        )

        addImageGalleryScreen(
            navController = navController,
            origin = "${PlantIdFeature.route()}${PlantDetailsFeature.baseRoute()}"
        )

    }
}