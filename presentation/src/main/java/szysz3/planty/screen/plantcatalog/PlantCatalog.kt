package szysz3.planty.screen.plantcatalog

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import szysz3.planty.core.model.PlantCatalogConfig
import szysz3.planty.navigation.FeatureRoute
import szysz3.planty.navigation.buildQueryString
import szysz3.planty.navigation.requiredIntArg
import szysz3.planty.navigation.staticComposable
import szysz3.planty.screen.plantcatalog.PlantCatalogFeature.PLANT_CATALOG_COLUMN_ARG_NAME
import szysz3.planty.screen.plantcatalog.PlantCatalogFeature.PLANT_CATALOG_CONFIG_ARG_NAME
import szysz3.planty.screen.plantcatalog.PlantCatalogFeature.PLANT_CATALOG_GARDEN_ID_ARG_NAME
import szysz3.planty.screen.plantcatalog.PlantCatalogFeature.PLANT_CATALOG_ROW_ARG_NAME
import szysz3.planty.screen.plantcatalog.screen.PlantCatalogScreen

object PlantCatalogFeature : FeatureRoute {
    const val TITLE = "Plants"
    const val PLANT_CATALOG_CONFIG_ARG_NAME = "config"
    const val PLANT_CATALOG_ROW_ARG_NAME = "row"
    const val PLANT_CATALOG_COLUMN_ARG_NAME = "column"
    const val PLANT_CATALOG_GARDEN_ID_ARG_NAME = "gardenId"

    private const val BASE_ROUTE = "/plantCatalog"

    override val basePath: String = BASE_ROUTE

    override val routeWithArgsPattern: String =
        "$basePath?" +
                "$PLANT_CATALOG_CONFIG_ARG_NAME={$PLANT_CATALOG_CONFIG_ARG_NAME}&" +
                "$PLANT_CATALOG_ROW_ARG_NAME={$PLANT_CATALOG_ROW_ARG_NAME}&" +
                "$PLANT_CATALOG_COLUMN_ARG_NAME={$PLANT_CATALOG_COLUMN_ARG_NAME}&" +
                "$PLANT_CATALOG_GARDEN_ID_ARG_NAME={$PLANT_CATALOG_GARDEN_ID_ARG_NAME}"

    fun routeWithArgs(
        origin: String = "",
        config: Int,
        row: Int? = null,
        column: Int? = null,
        gardenId: Int? = null
    ): String {
        val queryString = buildQueryString(
            PLANT_CATALOG_CONFIG_ARG_NAME to config,
            PLANT_CATALOG_ROW_ARG_NAME to row,
            PLANT_CATALOG_COLUMN_ARG_NAME to column,
            PLANT_CATALOG_GARDEN_ID_ARG_NAME to gardenId
        )
        return baseRoute(origin) + queryString
    }
}

fun NavGraphBuilder.addPlantCatalogScreen(
    origin: String = "",
    navController: NavHostController,
    onShowPlantDetails: (plantId: Int, row: Int?, column: Int?, gardenId: Int?) -> Unit,
) {
    staticComposable(
        route = PlantCatalogFeature.route(origin),
        arguments = listOf(
            navArgument(PLANT_CATALOG_CONFIG_ARG_NAME) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument(PLANT_CATALOG_GARDEN_ID_ARG_NAME) {
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
        val gardenId =
            backStackEntry.requiredIntArg(PLANT_CATALOG_GARDEN_ID_ARG_NAME)

        val config = backStackEntry.arguments
            ?.getString(PLANT_CATALOG_CONFIG_ARG_NAME)
            ?.toIntOrNull()

        val row = backStackEntry.arguments
            ?.getString(PLANT_CATALOG_ROW_ARG_NAME)
            ?.toIntOrNull()

        val column = backStackEntry.arguments
            ?.getString(PLANT_CATALOG_COLUMN_ARG_NAME)
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
            onShowPlantDetails = onShowPlantDetails,
            gardenId = gardenId
        )
    }
}
