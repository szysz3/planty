package szysz3.planty.screen.imagegallery

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import szysz3.planty.navigation.FeatureRoute
import szysz3.planty.navigation.buildQueryString
import szysz3.planty.navigation.requiredIntArg
import szysz3.planty.navigation.staticComposable
import szysz3.planty.screen.imagegallery.ImageGalleryFeature.IMAGE_GALLERY_PLANT_ID_ARG_NAME
import szysz3.planty.screen.imagegallery.screen.ImageGalleryScreen

object ImageGalleryFeature : FeatureRoute {
    const val TITLE = "Image Gallery"
    const val IMAGE_GALLERY_PLANT_ID_ARG_NAME = "plantId"

    private const val BASE_ROUTE = "/imageGallery"

    override val basePath: String = BASE_ROUTE

    override val routeWithArgsPattern: String =
        "$basePath?$IMAGE_GALLERY_PLANT_ID_ARG_NAME={$IMAGE_GALLERY_PLANT_ID_ARG_NAME}"

    fun routeWithArgs(
        origin: String = "",
        plantId: Int
    ): String {
        val qs = buildQueryString(IMAGE_GALLERY_PLANT_ID_ARG_NAME to plantId)
        return baseRoute(origin) + qs
    }
}

fun NavGraphBuilder.addImageGalleryScreen(
    origin: String = "",
    navController: NavHostController,
) {
    staticComposable(
        route = ImageGalleryFeature.route(origin),
        arguments = listOf(
            navArgument(IMAGE_GALLERY_PLANT_ID_ARG_NAME) {
                type = NavType.StringType
                nullable = true
            }
        )
    ) { backStackEntry ->
        val plantId = backStackEntry.requiredIntArg(IMAGE_GALLERY_PLANT_ID_ARG_NAME)

        ImageGalleryScreen(
            title = ImageGalleryFeature.TITLE,
            navController = navController,
            plantId = plantId
        )
    }
}
