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
    const val PLANT_DETAILS_ROW_ARG_NAME = "row"
    const val PLANT_DETAILS_COLUMN_ARG_NAME = "column"

    private const val ROUTE =
        "/plantDetails/{${PLANT_DETAILS_ORIGIN_ARG_NAME}}/{${PLANT_DETAILS_PLANT_ID_ARG_NAME}}/{${PLANT_DETAILS_ROW_ARG_NAME}}/{${PLANT_DETAILS_COLUMN_ARG_NAME}}"

    fun route() = ROUTE

    fun routeWithArgs(origin: Int, plantId: Int, row: Int? = null, column: Int? = null): String {
        return ROUTE.replace(
            "{${PLANT_DETAILS_ORIGIN_ARG_NAME}}",
            origin.toString()
        ).replace(
            "{${PLANT_DETAILS_PLANT_ID_ARG_NAME}}",
            plantId.toString()
        ).replace(
            "{${PLANT_DETAILS_ROW_ARG_NAME}}",
            row.toString()
        )
            .replace(
                "{${PLANT_DETAILS_COLUMN_ARG_NAME}}",
                column.toString()
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
            },
            navArgument(PlantDetailsFeature.PLANT_DETAILS_ROW_ARG_NAME) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(PlantDetailsFeature.PLANT_DETAILS_COLUMN_ARG_NAME) {
                type = NavType.StringType
                nullable = true
            }
        )) { backStackEntry ->

        val origin =
            backStackEntry.arguments?.getInt(PlantDetailsFeature.PLANT_DETAILS_ORIGIN_ARG_NAME)
                ?: throw IllegalArgumentException("Argument 'origin' is required and must not be null")
        val plantId =
            backStackEntry.arguments?.getInt(PlantDetailsFeature.PLANT_DETAILS_PLANT_ID_ARG_NAME)
                ?: throw IllegalArgumentException("Argument 'plantId' is required and must not be null")
        val row =
            backStackEntry.arguments?.getString(PlantDetailsFeature.PLANT_DETAILS_ROW_ARG_NAME)
                ?.toIntOrNull()
        val column =
            backStackEntry.arguments?.getString(PlantDetailsFeature.PLANT_DETAILS_COLUMN_ARG_NAME)
                ?.toIntOrNull()

        PlantDetailsScreen(
            title = PlantDetailsFeature.TITLE,
            navController = navController,
            origin = PlantDetailsScreenOrigin.fromValue(origin),
            plantId = plantId,
            row = row,
            column = column,
            onPlantChosen = onPlantChosen,
            onPlantImageClicked = onPlantImageClicked
        )
    }
}