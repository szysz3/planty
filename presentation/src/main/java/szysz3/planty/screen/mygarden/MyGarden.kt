package szysz3.planty.screen.mygarden

import MyGardenScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

object MyGardenFeature {
    const val ROUTE = "myGarden"
    const val TITLE = "My Garden"
}

fun NavGraphBuilder.addMyGardenScreen(navController: NavHostController) {
    composable(MyGardenFeature.ROUTE) {
        MyGardenScreen(
            title = MyGardenFeature.TITLE,
            navController = navController
        )
    }
}