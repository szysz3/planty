package szysz3.planty.screen.plantid

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import szysz3.planty.navigation.staticComposable
import szysz3.planty.screen.plantid.screen.PlantIdScreen

object PlantIdFeature {
    const val TITLE = "Plant Id"
    private const val BASE_ROUTE = "/plantId"

    fun route(origin: String = "") = "$origin$BASE_ROUTE"
}

fun NavGraphBuilder.addPlantIdScreen(
    origin: String = "",
    navController: NavHostController,
    onShowPlantDetails: (plantId: Int) -> Unit
) {
    staticComposable(
        route = PlantIdFeature.route(origin = origin),
    ) {
        PlantIdScreen(
            title = PlantIdFeature.TITLE,
            navController = navController,
            onShowPlantDetails = onShowPlantDetails
        )
    }
}