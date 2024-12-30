package szysz3.planty.screen.plantcatalog

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import szysz3.planty.core.model.PlantDetailsScreenOrigin
import szysz3.planty.navigation.staticComposable
import szysz3.planty.screen.plantcatalog.screen.PlantCatalogScreen

object PlantCatalogFeature {
    const val TITLE = "Plants"
    private const val ROUTE = "/plantCatalog"

    fun route() = ROUTE
}

fun NavGraphBuilder.addPlantCatalogScreen(
    navController: NavHostController,
    onShowPlantDetails: (origin: PlantDetailsScreenOrigin, plantId: Int) -> Unit,
) {
    staticComposable(
        route = PlantCatalogFeature.route(),
    ) {
        PlantCatalogScreen(
            title = PlantCatalogFeature.TITLE,
            navController = navController,
            onShowPlantDetails = onShowPlantDetails
        )
    }
}