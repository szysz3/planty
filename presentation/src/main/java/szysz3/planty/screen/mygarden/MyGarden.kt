package szysz3.planty.screen.mygarden

import MyGardenScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import szysz3.planty.navigation.staticComposable

object MyGardenFeature {
    const val TITLE = "My Garden"
    private const val BASE_ROUTE = "/myGarden"

    fun route(origin: String = "") = "$origin$BASE_ROUTE"
}

fun NavGraphBuilder.addMyGardenScreen(
    origin: String = "",
    navController: NavHostController,
    onPlantChosen: (plantId: Int, row: Int, column: Int) -> Unit,
    onEmptyGardenFieldChosen: (row: Int, column: Int) -> Unit,
) {
    staticComposable(
        route = MyGardenFeature.route(origin = origin),
    ) {
        MyGardenScreen(
            title = MyGardenFeature.TITLE,
            navController = navController,
            onPlantChosen = onPlantChosen,
            onEmptyGardenFieldChosen = onEmptyGardenFieldChosen
        )
    }
}