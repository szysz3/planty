package szysz3.planty.screen.plantcatalog

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import szysz3.planty.core.model.PlantCatalogScreenOrigin
import szysz3.planty.navigation.staticComposable
import szysz3.planty.screen.plantcatalog.screen.PlantCatalogScreen

object PlantCatalogFeature {
    const val TITLE = "Plants"
    const val PLANT_CATALOG_ROW_ARG_NAME = "row"
    const val PLANT_CATALOG_COLUMN_ARG_NAME = "column"
    const val PLANT_CATALOG_ORIGIN_ARG_NAME = "origin"

    private const val BASE_ROUTE = "/plantCatalog"

    fun route(origin: String = "") = "$origin$BASE_ROUTE"

    fun routeWithArgs(
        origin: String = "",
        row: Int? = null,
        column: Int? = null,
    ): String {
        val queryParams = buildList {
            row?.let { add("$PLANT_CATALOG_ROW_ARG_NAME=$it") }
            column?.let { add("$PLANT_CATALOG_COLUMN_ARG_NAME=$it") }
        }

        return if (queryParams.isNotEmpty()) {
            "${route(origin)}?${queryParams.joinToString("&")}"
        } else {
            route(origin)
        }
    }
}

fun NavGraphBuilder.addPlantCatalogScreen(
    origin: String = "",
    navController: NavHostController,
    onShowPlantDetails: (origin: PlantCatalogScreenOrigin, plantId: Int, row: Int?, column: Int?) -> Unit,
) {
    staticComposable(
        route = PlantCatalogFeature.route(origin = origin),
        arguments = listOf(
            navArgument(PlantCatalogFeature.PLANT_CATALOG_ROW_ARG_NAME) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(PlantCatalogFeature.PLANT_CATALOG_COLUMN_ARG_NAME) {
                type = NavType.StringType
                nullable = true
            }
        )
    ) { backStackEntry ->
        val row =
            backStackEntry.arguments?.getString(PlantCatalogFeature.PLANT_CATALOG_ROW_ARG_NAME)
                ?.toIntOrNull()
        val column =
            backStackEntry.arguments?.getString(PlantCatalogFeature.PLANT_CATALOG_COLUMN_ARG_NAME)
                ?.toIntOrNull()
        val origin =
            backStackEntry.arguments?.getInt(PlantCatalogFeature.PLANT_CATALOG_ORIGIN_ARG_NAME)
                ?: throw IllegalArgumentException("Argument 'origin' is required and must not be null")

        PlantCatalogScreen(
            title = PlantCatalogFeature.TITLE,
            navController = navController,
            origin = PlantCatalogScreenOrigin.fromValue(origin),
            row = row,
            column = column,
            onShowPlantDetails = onShowPlantDetails
        )
    }
}