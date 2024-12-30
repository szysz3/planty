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

    private const val ROUTE = "/imageGallery/{${IMAGE_GALLERY_PLANT_ID_ARG_NAME}}"

    fun route() = ROUTE

    fun routeWithArgs(plantId: Int): String {
        return ROUTE.replace(
            "{${IMAGE_GALLERY_PLANT_ID_ARG_NAME}}",
            plantId.toString()
        )
    }
}

fun NavGraphBuilder.addImageGalleryScreen(
    navController: NavHostController,
) {
    staticComposable(
        route = ImageGalleryFeature.route(),
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