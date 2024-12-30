package szysz3.planty.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Search
import szysz3.planty.screen.mygarden.MyGardenFeature
import szysz3.planty.screen.plantcatalog.PlantCatalogFeature
import szysz3.planty.screen.plantid.PlantIdFeature
import szysz3.planty.screen.tasklist.TaskListFeature

object BottomBarNavigation {
    val MyGarden = BottomNavItem(MyGardenFeature.route(), MyGardenFeature.TITLE, Icons.Rounded.Home)
    val TaskList = BottomNavItem(TaskListFeature.route(), TaskListFeature.TITLE, Icons.Rounded.Done)
    val PlantId = BottomNavItem(PlantIdFeature.route(), PlantIdFeature.TITLE, Icons.Rounded.Search)
    val PlantCatalog =
        BottomNavItem(PlantCatalogFeature.route(), PlantCatalogFeature.TITLE, Icons.Rounded.Info)

}