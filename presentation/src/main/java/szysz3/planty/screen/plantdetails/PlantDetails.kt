package szysz3.planty.screen.plantdetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import szysz3.planty.core.model.PlantDetailsScreenOrigin
import szysz3.planty.navigation.staticComposable
import szysz3.planty.screen.plantdetails.screen.PlantDetailsScreen

object PlantDetailsFeature {
    const val TITLE = "Plant Details"
    const val PLANT_DETAILS_ORIGIN_ARG_NAME = "origin"
    const val PLANT_DETAILS_PLANT_ID_ARG_NAME = "plantId"

    private const val ROUTE =
        "/plantDetails/{${PLANT_DETAILS_ORIGIN_ARG_NAME}}/{${PLANT_DETAILS_PLANT_ID_ARG_NAME}}"

    fun route() = ROUTE

    fun routeWithArgs(origin: Int, plantId: Int): String {
        return ROUTE.replace(
            "{${PLANT_DETAILS_ORIGIN_ARG_NAME}}",
            origin.toString()
        ).replace(
            "{${PLANT_DETAILS_PLANT_ID_ARG_NAME}}",
            plantId.toString()
        )
    }
}

fun NavGraphBuilder.addPlantDetailsScreen(
    navController: NavHostController,
    onPlantChosen: () -> Unit,
    onPlantImageClicked: (plantId: Int) -> Unit,
) {
    staticComposable(
        route = PlantDetailsFeature.route(),
        arguments = listOf(
            navArgument(PlantDetailsFeature.PLANT_DETAILS_ORIGIN_ARG_NAME) {
                type = NavType.IntType
                nullable = false
            },
            navArgument(PlantDetailsFeature.PLANT_DETAILS_PLANT_ID_ARG_NAME) {
                type = NavType.IntType
                nullable = false
            }
        )) { backStackEntry ->

        val origin =
            backStackEntry.arguments?.getInt(PlantDetailsFeature.PLANT_DETAILS_ORIGIN_ARG_NAME)
                ?: throw IllegalArgumentException("Argument 'origin' is required and must not be null")
        val plantId =
            backStackEntry.arguments?.getInt(PlantDetailsFeature.PLANT_DETAILS_PLANT_ID_ARG_NAME)
                ?: throw IllegalArgumentException("Argument 'plantId' is required and must not be null")


        PlantDetailsScreen(
            title = PlantDetailsFeature.TITLE,
            navController = navController,
            origin = PlantDetailsScreenOrigin.fromValue(origin),
            plantId = plantId,
            onPlantChosen = onPlantChosen,
            onPlantImageClicked = onPlantImageClicked
        )
    }
}