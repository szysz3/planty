package szysz3.planty.screen.plantcatalog

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import szysz3.planty.core.model.PlantDetailsScreenOrigin
import szysz3.planty.navigation.staticComposable
import szysz3.planty.screen.plantcatalog.screen.PlantCatalogScreen

object PlantCatalogFeature {
    const val TITLE = "Plants"
    const val PLANT_CATALOG_ROW_ARG_NAME = "row"
    const val PLANT_CATALOG_COLUMN_ARG_NAME = "column"

    private const val ROUTE =
        "/plantCatalog/{$PLANT_CATALOG_ROW_ARG_NAME}/{${PLANT_CATALOG_COLUMN_ARG_NAME}}"

    fun route() = ROUTE

    fun routeWithArgs(row: Int? = null, column: Int? = null): String {
        return ROUTE.replace(
            "{$PLANT_CATALOG_ROW_ARG_NAME}",
            row.toString()
        ).replace(
            "{$PLANT_CATALOG_COLUMN_ARG_NAME}",
            column.toString()
        )
    }
}

fun NavGraphBuilder.addPlantCatalogScreen(
    navController: NavHostController,
    onShowPlantDetails: (origin: PlantDetailsScreenOrigin, plantId: Int, row: Int?, column: Int?) -> Unit,
) {
    staticComposable(
        route = PlantCatalogFeature.route(),
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

        PlantCatalogScreen(
            title = PlantCatalogFeature.TITLE,
            navController = navController,
            row = row,
            column = column,
            onShowPlantDetails = onShowPlantDetails
        )
    }
}