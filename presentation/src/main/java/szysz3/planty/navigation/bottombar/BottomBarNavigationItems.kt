package szysz3.planty.navigation.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Search
import szysz3.planty.screen.mygarden.MyGardenFeature
import szysz3.planty.screen.plantcatalog.PlantCatalogFeature
import szysz3.planty.screen.plantid.PlantIdFeature
import szysz3.planty.screen.tasklist.TaskListFeature

object BottomBarNavigationItems {
    private const val GRAPH = "graph"

    val MyGarden = BottomNavItem(
        route = MyGardenFeature.route(origin = GRAPH),
        title = MyGardenFeature.TITLE,
        icon = Icons.Rounded.Home,
        graphRoute = GRAPH
    )

    val TaskList = BottomNavItem(
        route = TaskListFeature.route(origin = GRAPH),
        title = TaskListFeature.TITLE,
        icon = Icons.Rounded.Done,
        graphRoute = GRAPH
    )

    val PlantId = BottomNavItem(
        route = PlantIdFeature.route(origin = GRAPH),
        title = PlantIdFeature.TITLE,
        icon = Icons.Rounded.Search,
        graphRoute = GRAPH
    )

    val PlantCatalog = BottomNavItem(
        route = PlantCatalogFeature.route(origin = GRAPH),
        title = PlantCatalogFeature.TITLE,
        icon = Icons.Rounded.Info,
        graphRoute = GRAPH
    )

    val items = listOf(
        MyGarden,
        TaskList,
        PlantId,
        PlantCatalog
    )
}
