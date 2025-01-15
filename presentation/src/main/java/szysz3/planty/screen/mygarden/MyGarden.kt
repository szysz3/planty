package szysz3.planty.screen.mygarden

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import szysz3.planty.navigation.FeatureRoute
import szysz3.planty.navigation.staticComposable
import szysz3.planty.screen.mygarden.screen.MyGardenScreen

object MyGardenFeature : FeatureRoute {
    const val TITLE = "My Garden"

    override val basePath: String = "/myGarden"

    override val routeWithArgsPattern: String = basePath
}

fun NavGraphBuilder.addMyGardenScreen(
    origin: String = "",
    navController: NavHostController,
    onPlantChosen: (plantId: Int, row: Int, column: Int, gardenId: Int) -> Unit,
    onEmptyGardenFieldChosen: (row: Int, column: Int, gardenId: Int) -> Unit,
) {
    staticComposable(
        route = MyGardenFeature.route(origin),
    ) {
        MyGardenScreen(
            title = MyGardenFeature.TITLE,
            navController = navController,
            onPlantChosen = onPlantChosen,
            onEmptyGardenFieldChosen = onEmptyGardenFieldChosen
        )
    }
}
