package szysz3.planty.screen.plantcatalog

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import szysz3.planty.core.model.PlantCatalogConfig
import szysz3.planty.navigation.staticComposable
import szysz3.planty.screen.plantcatalog.PlantCatalogFeature.PLANT_CATALOG_COLUMN_ARG_NAME
import szysz3.planty.screen.plantcatalog.PlantCatalogFeature.PLANT_CATALOG_CONFIG_ARG_NAME
import szysz3.planty.screen.plantcatalog.PlantCatalogFeature.PLANT_CATALOG_ROW_ARG_NAME
import szysz3.planty.screen.plantcatalog.screen.PlantCatalogScreen

object PlantCatalogFeature {
    const val TITLE = "Plants"
    const val PLANT_CATALOG_CONFIG_ARG_NAME = "config"
    const val PLANT_CATALOG_ROW_ARG_NAME = "row"
    const val PLANT_CATALOG_COLUMN_ARG_NAME = "column"

    private const val BASE_ROUTE = "/plantCatalog"

    private const val ROUTE_WITH_ARGS =
        "$BASE_ROUTE?${PLANT_CATALOG_CONFIG_ARG_NAME}={${PLANT_CATALOG_CONFIG_ARG_NAME}}&${PLANT_CATALOG_ROW_ARG_NAME}={${PLANT_CATALOG_ROW_ARG_NAME}}&${PLANT_CATALOG_COLUMN_ARG_NAME}={${PLANT_CATALOG_COLUMN_ARG_NAME}}"

    fun baseRoute(origin: String = "") = "$origin$BASE_ROUTE"

    fun route(origin: String = ""): String {
        val registeredRoute = "$origin$ROUTE_WITH_ARGS"
        return registeredRoute
    }

    fun routeWithArgs(
        origin: String = "",
        config: Int,
        row: Int? = null,
        column: Int? = null,
    ): String {
        val queryParams = buildList {
            add("$PLANT_CATALOG_CONFIG_ARG_NAME=$config")
            row?.let { add("$PLANT_CATALOG_ROW_ARG_NAME=$it") }
            column?.let { add("$PLANT_CATALOG_COLUMN_ARG_NAME=$it") }
        }

        val finalRoute = if (queryParams.isNotEmpty()) {
            "${baseRoute(origin)}?${queryParams.joinToString("&")}"
        } else {
            baseRoute(origin)
        }

        return finalRoute
    }
}

fun NavGraphBuilder.addPlantCatalogScreen(
    origin: String = "",
    navController: NavHostController,
    onShowPlantDetails: (plantId: Int, row: Int?, column: Int?) -> Unit,
) {
    staticComposable(
        route = PlantCatalogFeature.route(origin = origin),
        arguments = listOf(
            navArgument(PLANT_CATALOG_CONFIG_ARG_NAME) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument(PLANT_CATALOG_ROW_ARG_NAME) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument(PLANT_CATALOG_COLUMN_ARG_NAME) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) { backStackEntry ->
        val config =
            backStackEntry.arguments?.getString(PLANT_CATALOG_CONFIG_ARG_NAME)
                ?.toIntOrNull()
        val row =
            backStackEntry.arguments?.getString(PLANT_CATALOG_ROW_ARG_NAME)
                ?.toIntOrNull()
        val column =
            backStackEntry.arguments?.getString(PLANT_CATALOG_COLUMN_ARG_NAME)
                ?.toIntOrNull()

        PlantCatalogScreen(
            title = PlantCatalogFeature.TITLE,
            navController = navController,
            origin = if (config != null) {
                PlantCatalogConfig.fromValue(config)
            } else {
                PlantCatalogConfig.PREVIEW
            },
            row = row,
            column = column,
            onShowPlantDetails = onShowPlantDetails
        )
    }
}