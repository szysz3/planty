package szysz3.planty.screen.imagegallery

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import szysz3.planty.navigation.staticComposable
import szysz3.planty.screen.imagegallery.ImageGalleryFeature.IMAGE_GALLERY_PLANT_ID_ARG_NAME
import szysz3.planty.screen.imagegallery.screen.ImageGalleryScreen
import szysz3.planty.screen.plantdetails.PlantDetailsFeature.PLANT_DETAILS_PLANT_ID_ARG_NAME

object ImageGalleryFeature {
    const val TITLE = "Image Gallery"
    const val IMAGE_GALLERY_PLANT_ID_ARG_NAME = "plantId"

    private const val BASE_ROUTE = "/imageGallery"

    private const val BASE_ROUTE_WITH_ARGS =
        "$BASE_ROUTE?$IMAGE_GALLERY_PLANT_ID_ARG_NAME={${IMAGE_GALLERY_PLANT_ID_ARG_NAME}}"

    private fun baseRoute(origin: String = "") = "$origin${BASE_ROUTE}"

    fun route(origin: String = "") = "$origin${BASE_ROUTE_WITH_ARGS}"

    fun routeWithArgs(
        origin: String = "",
        plantId: Int
    ): String {
        val queryParams = buildList {
            add("$PLANT_DETAILS_PLANT_ID_ARG_NAME=$plantId")
        }

        val finalRoute = if (queryParams.isNotEmpty()) {
            "${baseRoute(origin)}?${queryParams.joinToString("&")}"
        } else {
            baseRoute(origin)
        }

        return finalRoute
    }
}

fun NavGraphBuilder.addImageGalleryScreen(
    origin: String = "",
    navController: NavHostController,
) {
    staticComposable(
        route = ImageGalleryFeature.route(origin = origin),
        arguments = listOf(
            navArgument(IMAGE_GALLERY_PLANT_ID_ARG_NAME) {
                type = NavType.StringType
                nullable = true
            }
        )) { backStackEntry ->

        val plantId =
            backStackEntry.arguments?.getString(IMAGE_GALLERY_PLANT_ID_ARG_NAME)
                ?: throw IllegalArgumentException("Argument 'plantId' is required and must not be null")

        ImageGalleryScreen(
            title = ImageGalleryFeature.TITLE,
            navController = navController,
            plantId = plantId.toInt()
        )
    }
}