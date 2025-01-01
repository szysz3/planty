package szysz3.planty.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import szysz3.planty.navigation.bottombar.BottomBarNavigationItems

@Composable
fun MainNavigationGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarNavigationItems.MyGarden.route,
    ) {
        myGardenGraph(navController)
        taskListGraph(navController)
        plantCatalogGraph(navController)
        plantIdGraph(navController)
    }
}