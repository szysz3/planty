package szysz3.planty.screen.plantdetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import szysz3.planty.navigation.staticComposable
import szysz3.planty.screen.plantdetails.screen.PlantDetailsScreen

object PlantDetailsFeature {
    const val TITLE = "Plant Details"
    const val PLANT_DETAILS_PLANT_ID_ARG_NAME = "plantId"
    const val PLANT_DETAILS_ROW_ARG_NAME = "row"
    const val PLANT_DETAILS_COLUMN_ARG_NAME = "column"

    private const val BASE_ROUTE = "/plantDetails/{${PLANT_DETAILS_PLANT_ID_ARG_NAME}}"

    fun route(origin: String = "") = "$origin${BASE_ROUTE}"

    fun routeWithArgs(
        origin: String = "",
        plantId: Int,
        row: Int? = null,
        column: Int? = null
    ): String {
        val route = route(origin).replace(
            "{${PLANT_DETAILS_PLANT_ID_ARG_NAME}}",
            plantId.toString()
        )

        val queryParams = buildList {
            row?.let { add("$PLANT_DETAILS_ROW_ARG_NAME=$it") }
            column?.let { add("$PLANT_DETAILS_COLUMN_ARG_NAME=$it") }
        }

        return if (queryParams.isNotEmpty()) {
            "${route}?${queryParams.joinToString("&")}"
        } else {
            route
        }
    }
}

fun NavGraphBuilder.addPlantDetailsScreen(
    origin: String = "",
    navController: NavHostController,
    onPlantChosen: (() -> Unit)? = null,
    onPlantImageClicked: (plantId: Int) -> Unit,
) {
    staticComposable(
        route = PlantDetailsFeature.route(origin),
        arguments = listOf(
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
            plantId = plantId,
            row = row,
            column = column,
            onPlantChosen = onPlantChosen,
            onPlantImageClicked = onPlantImageClicked
        )
    }
}