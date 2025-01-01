package szysz3.planty.screen.plantdetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import szysz3.planty.core.model.PlantDetailsConfig
import szysz3.planty.navigation.staticComposable
import szysz3.planty.screen.plantdetails.PlantDetailsFeature.PLANT_DETAILS_COLUMN_ARG_NAME
import szysz3.planty.screen.plantdetails.PlantDetailsFeature.PLANT_DETAILS_CONFIG_ARG_NAME
import szysz3.planty.screen.plantdetails.PlantDetailsFeature.PLANT_DETAILS_PLANT_ID_ARG_NAME
import szysz3.planty.screen.plantdetails.PlantDetailsFeature.PLANT_DETAILS_ROW_ARG_NAME
import szysz3.planty.screen.plantdetails.PlantDetailsFeature.TITLE
import szysz3.planty.screen.plantdetails.screen.PlantDetailsScreen

object PlantDetailsFeature {
    const val TITLE = "Plant Details"
    const val PLANT_DETAILS_PLANT_ID_ARG_NAME = "plantId"
    const val PLANT_DETAILS_CONFIG_ARG_NAME = "config"
    const val PLANT_DETAILS_ROW_ARG_NAME = "row"
    const val PLANT_DETAILS_COLUMN_ARG_NAME = "column"

    private const val BASE_ROUTE = "/plantDetails"

    private const val ROUTE_WITH_ARGS =
        "$BASE_ROUTE?${PLANT_DETAILS_PLANT_ID_ARG_NAME}={${PLANT_DETAILS_PLANT_ID_ARG_NAME}}&${PLANT_DETAILS_CONFIG_ARG_NAME}={${PLANT_DETAILS_CONFIG_ARG_NAME}}&${PLANT_DETAILS_ROW_ARG_NAME}={${PLANT_DETAILS_ROW_ARG_NAME}}&${PLANT_DETAILS_COLUMN_ARG_NAME}={${PLANT_DETAILS_COLUMN_ARG_NAME}}"

    fun baseRoute(origin: String = "") = "$origin${BASE_ROUTE}"

    fun route(origin: String = ""): String {
        val registeredRoute = "$origin${ROUTE_WITH_ARGS}"
        return registeredRoute
    }

    fun routeWithArgs(
        origin: String = "",
        plantId: Int,
        config: Int,
        row: Int? = null,
        column: Int? = null
    ): String {
        val queryParams = buildList {
            add("$PLANT_DETAILS_PLANT_ID_ARG_NAME=$plantId")
            add("$PLANT_DETAILS_CONFIG_ARG_NAME=$config")
            row?.let { add("$PLANT_DETAILS_ROW_ARG_NAME=$it") }
            column?.let { add("$PLANT_DETAILS_COLUMN_ARG_NAME=$it") }
        }

        val finalRoute = if (queryParams.isNotEmpty()) {
            "${baseRoute(origin)}?${queryParams.joinToString("&")}"
        } else {
            baseRoute(origin)
        }

        return finalRoute
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
            navArgument(PLANT_DETAILS_PLANT_ID_ARG_NAME) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument(PLANT_DETAILS_CONFIG_ARG_NAME) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument(PLANT_DETAILS_ROW_ARG_NAME) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument(PLANT_DETAILS_COLUMN_ARG_NAME) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )) { backStackEntry ->
        val plantId =
            backStackEntry.arguments?.getString(PLANT_DETAILS_PLANT_ID_ARG_NAME)
                ?: throw IllegalArgumentException("Argument 'plantId' is required and must not be null")
        val config =
            backStackEntry.arguments?.getString(PLANT_DETAILS_CONFIG_ARG_NAME)
                ?: throw IllegalArgumentException("Argument 'config' is required and must not be null")
        val row =
            backStackEntry.arguments?.getString(PLANT_DETAILS_ROW_ARG_NAME)
                ?.toIntOrNull()
        val column =
            backStackEntry.arguments?.getString(PLANT_DETAILS_COLUMN_ARG_NAME)
                ?.toIntOrNull()

        PlantDetailsScreen(
            title = TITLE,
            navController = navController,
            config = PlantDetailsConfig.fromValue(config.toInt()),
            plantId = plantId.toInt(),
            row = row,
            column = column,
            onPlantChosen = onPlantChosen,
            onPlantImageClicked = onPlantImageClicked
        )
    }
}