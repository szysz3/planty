package szysz3.planty.screen.imagegallery

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import szysz3.planty.navigation.staticComposable
import szysz3.planty.screen.imagegallery.ImageGalleryFeature.IMAGE_GALLERY_PLANT_ID_ARG_NAME
import szysz3.planty.screen.imagegallery.screen.ImageGalleryScreen

object ImageGalleryFeature {
    const val TITLE = "Image Gallery"
    const val IMAGE_GALLERY_PLANT_ID_ARG_NAME = "plantId"

    private const val BASE_ROUTE = "/imageGallery/{${IMAGE_GALLERY_PLANT_ID_ARG_NAME}}"

    fun route(origin: String = "") = "$origin${BASE_ROUTE}"

    fun routeWithArgs(origin: String = "", plantId: Int): String {
        return route(origin).replace(
            "{${IMAGE_GALLERY_PLANT_ID_ARG_NAME}}",
            plantId.toString()
        )
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
                type = NavType.IntType
                nullable = false
            }
        )) { backStackEntry ->

        val plantId =
            backStackEntry.arguments?.getInt(IMAGE_GALLERY_PLANT_ID_ARG_NAME)
                ?: throw IllegalArgumentException("Argument 'plantId' is required and must not be null")

        ImageGalleryScreen(
            title = ImageGalleryFeature.TITLE,
            navController = navController,
            plantId = plantId
        )
    }
}