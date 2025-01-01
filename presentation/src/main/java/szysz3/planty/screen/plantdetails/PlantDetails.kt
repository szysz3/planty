package szysz3.planty.screen.plantdetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import szysz3.planty.core.model.PlantDetailsConfig
import szysz3.planty.navigation.FeatureRoute
import szysz3.planty.navigation.buildQueryString
import szysz3.planty.navigation.requiredIntArg
import szysz3.planty.navigation.staticComposable
import szysz3.planty.screen.plantdetails.screen.PlantDetailsScreen

object PlantDetailsFeature : FeatureRoute {
    const val TITLE = "Plant Details"

    const val PLANT_DETAILS_PLANT_ID_ARG_NAME = "plantId"
    const val PLANT_DETAILS_CONFIG_ARG_NAME = "config"
    const val PLANT_DETAILS_ROW_ARG_NAME = "row"
    const val PLANT_DETAILS_COLUMN_ARG_NAME = "column"

    override val basePath: String = "/plantDetails"

    override val routeWithArgsPattern: String =
        "$basePath?" +
                "$PLANT_DETAILS_PLANT_ID_ARG_NAME={$PLANT_DETAILS_PLANT_ID_ARG_NAME}&" +
                "$PLANT_DETAILS_CONFIG_ARG_NAME={$PLANT_DETAILS_CONFIG_ARG_NAME}&" +
                "$PLANT_DETAILS_ROW_ARG_NAME={$PLANT_DETAILS_ROW_ARG_NAME}&" +
                "$PLANT_DETAILS_COLUMN_ARG_NAME={$PLANT_DETAILS_COLUMN_ARG_NAME}"

    fun routeWithArgs(
        origin: String = "",
        plantId: Int,
        config: Int,
        row: Int? = null,
        column: Int? = null
    ): String {
        val queryString = buildQueryString(
            PLANT_DETAILS_PLANT_ID_ARG_NAME to plantId,
            PLANT_DETAILS_CONFIG_ARG_NAME to config,
            PLANT_DETAILS_ROW_ARG_NAME to row,
            PLANT_DETAILS_COLUMN_ARG_NAME to column
        )
        return baseRoute(origin) + queryString
    }
}

fun NavGraphBuilder.addPlantDetailsScreen(
    origin: String = "",
    navController: NavHostController,
    onPlantChosen: (() -> Unit)? = null,
    onPlantImageClicked: (plantId: Int) -> Unit
) {
    staticComposable(
        route = PlantDetailsFeature.route(origin),
        arguments = listOf(
            navArgument(PlantDetailsFeature.PLANT_DETAILS_PLANT_ID_ARG_NAME) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(PlantDetailsFeature.PLANT_DETAILS_CONFIG_ARG_NAME) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(PlantDetailsFeature.PLANT_DETAILS_ROW_ARG_NAME) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(PlantDetailsFeature.PLANT_DETAILS_COLUMN_ARG_NAME) {
                type = NavType.StringType
                nullable = true
            }
        )
    ) { backStackEntry ->
        val plantId =
            backStackEntry.requiredIntArg(PlantDetailsFeature.PLANT_DETAILS_PLANT_ID_ARG_NAME)
        val config =
            backStackEntry.requiredIntArg(PlantDetailsFeature.PLANT_DETAILS_CONFIG_ARG_NAME)

        val row = backStackEntry.arguments
            ?.getString(PlantDetailsFeature.PLANT_DETAILS_ROW_ARG_NAME)
            ?.toIntOrNull()

        val column = backStackEntry.arguments
            ?.getString(PlantDetailsFeature.PLANT_DETAILS_COLUMN_ARG_NAME)
            ?.toIntOrNull()

        PlantDetailsScreen(
            title = PlantDetailsFeature.TITLE,
            navController = navController,
            config = PlantDetailsConfig.fromValue(config),
            plantId = plantId,
            row = row,
            column = column,
            onPlantChosen = onPlantChosen,
            onPlantImageClicked = onPlantImageClicked
        )
    }
}