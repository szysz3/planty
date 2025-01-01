package szysz3.planty.screen.plantid

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import szysz3.planty.navigation.FeatureRoute
import szysz3.planty.navigation.staticComposable
import szysz3.planty.screen.plantid.screen.PlantIdScreen

object PlantIdFeature : FeatureRoute {
    const val TITLE = "Plant Id"

    private const val BASE_ROUTE = "/plantId"

    override val basePath: String = BASE_ROUTE

    override val routeWithArgsPattern: String = basePath
}

fun NavGraphBuilder.addPlantIdScreen(
    origin: String = "",
    navController: NavHostController,
    onShowPlantDetails: (plantId: Int) -> Unit
) {
    staticComposable(
        route = PlantIdFeature.route(origin),
    ) {
        PlantIdScreen(
            title = PlantIdFeature.TITLE,
            navController = navController,
            onShowPlantDetails = onShowPlantDetails
        )
    }
}