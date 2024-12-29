package szysz3.planty.screen.mygarden

import MyGardenScreen
import androidx.compose.animation.fadeIn
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

object MyGardenFeature {
    const val TITLE = "My Garden"
    private const val ROUTE = "myGarden"

    fun route() = ROUTE
}

fun NavGraphBuilder.addMyGardenScreen(navController: NavHostController) {
    composable(
        route = MyGardenFeature.route(),
        enterTransition = {
            fadeIn()
        },
        exitTransition = {
            null
        },
    ) {
        MyGardenScreen(
            title = MyGardenFeature.TITLE,
            navController = navController
        )
    }
}