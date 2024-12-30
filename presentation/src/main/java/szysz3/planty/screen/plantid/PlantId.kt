package szysz3.planty.screen.plantid

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import szysz3.planty.navigation.staticComposable
import szysz3.planty.screen.plantid.screen.PlantIdScreen

object PlantIdFeature {
    const val TITLE = "Plant Id"
    private const val ROUTE = "plantId"

    fun route() = ROUTE
}

fun NavGraphBuilder.addPlantIdScreen(
    navController: NavHostController,
    onShowPlantDetails: (plantId: Int) -> Unit
) {
    staticComposable(
        route = PlantIdFeature.route(),
    ) {
        PlantIdScreen(
            title = PlantIdFeature.TITLE,
            navController = navController,
            onShowPlantDetails = onShowPlantDetails
        )
    }
}