package szysz3.planty.screen.mygarden

import MyGardenScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import szysz3.planty.navigation.FeatureRoute
import szysz3.planty.navigation.staticComposable

object MyGardenFeature : FeatureRoute {
    const val TITLE = "My Garden"

    override val basePath: String = "/myGarden"

    override val routeWithArgsPattern: String = basePath
}

fun NavGraphBuilder.addMyGardenScreen(
    origin: String = "",
    navController: NavHostController,
    onPlantChosen: (plantId: Int, row: Int, column: Int) -> Unit,
    onEmptyGardenFieldChosen: (row: Int, column: Int) -> Unit,
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
