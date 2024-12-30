package szysz3.planty.screen.mygarden

import MyGardenScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import szysz3.planty.core.model.PlantDetailsScreenOrigin
import szysz3.planty.navigation.staticComposable

object MyGardenFeature {
    const val TITLE = "My Garden"
    private const val ROUTE = "myGarden"

    fun route() = ROUTE
}

fun NavGraphBuilder.addMyGardenScreen(
    navController: NavHostController,
    onPlantChosen: (origin: PlantDetailsScreenOrigin, plantId: Int) -> Unit,
    onGardenFieldChosen: (row: Int, column: Int) -> Unit,
) {
    staticComposable(
        route = MyGardenFeature.route(),
    ) {
        MyGardenScreen(
            title = MyGardenFeature.TITLE,
            navController = navController,
            onPlantChosen = onPlantChosen,
            onGardenFieldChosen = onGardenFieldChosen
        )
    }
}