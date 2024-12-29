package szysz3.planty.screen.mygarden

import MyGardenScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

object MyGardenFeature {
    const val TITLE = "My Garden"
    private const val ROUTE = "myGarden"

    fun route() = ROUTE
}

fun NavGraphBuilder.addMyGardenScreen(navController: NavHostController) {
    composable(MyGardenFeature.route()) {
        MyGardenScreen(
            title = MyGardenFeature.TITLE,
            navController = navController
        )
    }
}