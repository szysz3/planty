package szysz3.planty.screen.plantid

import androidx.compose.animation.fadeIn
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
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
    composable(
        route = PlantIdFeature.route(),
        enterTransition = {
            fadeIn()
        },
        exitTransition = {
            null
        },
    ) {
        PlantIdScreen(
            title = PlantIdFeature.TITLE,
            navController = navController,
            onShowPlantDetails = onShowPlantDetails
        )
    }
}