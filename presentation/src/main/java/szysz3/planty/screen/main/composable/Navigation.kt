package szysz3.planty.screen.main.composable

import MyGardenScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.mygarden.viewmodel.MyGardenViewModel
import szysz3.planty.screen.notification.NotificationsScreen
import szysz3.planty.screen.plantaplant.screen.PlantAPlantScreen
import szysz3.planty.screen.plantaplant.viewmodel.PlantAPlantViewModel
import szysz3.planty.screen.plantdetails.screen.PlantDetailsScreen
import szysz3.planty.screen.plantdetails.screen.PlantDetailsScreenOrigin
import szysz3.planty.screen.tasklist.TaskListScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    mainScreenViewModel: MainScreenViewModel,
    myGardenViewModel: MyGardenViewModel,
    plantAPlantViewModel: PlantAPlantViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Home.route) {
            MyGardenScreen(
                mainScreenViewModel = mainScreenViewModel,
                myGardenViewModel = myGardenViewModel,
                onNavigateToPlantAPlant = {
                    navigate(navController, NavigationItem.PlantAPlant)
                },
                onNavigateToPlantDetails = { origin ->
                    navigate(navController, NavigationItem.PlantDetails.withArgs(origin.value))
                }
            )
        }
        composable(BottomNavItem.TaskList.route) {
            TaskListScreen(
                mainScreenViewModel = mainScreenViewModel
            )
        }
        composable(BottomNavItem.Notifications.route) {
            NotificationsScreen()
        }
        composable(
            route = NavigationItem.PlantDetails.route,
            arguments = listOf(
                navArgument(NavigationItem.PLANT_DETAILS_ARG_NAME) {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val origin =
                backStackEntry.arguments?.getInt(NavigationItem.PLANT_DETAILS_ARG_NAME) ?: 0
            PlantDetailsScreen(
                mainScreenViewModel = mainScreenViewModel,
                plantAPlantViewModel = plantAPlantViewModel,
                myGardenViewModel = myGardenViewModel,
                origin = PlantDetailsScreenOrigin.fromValue(origin),
                onPlantChosen = {
                    navController.popBackStack(BottomNavItem.Home.route, false)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(NavigationItem.PlantAPlant.route) {
            PlantAPlantScreen(
                mainScreenViewModel = mainScreenViewModel,
                plantAPlantViewModel = plantAPlantViewModel,
            ) { origin ->
                navigate(navController, NavigationItem.PlantDetails.withArgs(origin.value))
            }
        }
    }
}

fun navigate(navController: NavHostController, navigationItem: NavigationItem) {
    navController.navigate(navigationItem.route) {
        launchSingleTop = true
        restoreState = true
    }
}

open class NavigationItem(val route: String, val title: String) {
    object PlantAPlant : NavigationItem("/myGarden/plantAPlant", "Plant a plant")
    object PlantDetails :
        NavigationItem("/myGarden/plantDetails/{${PLANT_DETAILS_ARG_NAME}}", "Plant details") {
        fun withArgs(origin: Int): NavigationItem {
            return NavigationItem(
                route.replace(
                    "{${PLANT_DETAILS_ARG_NAME}}",
                    origin.toString()
                ), title
            )
        }
    }

    companion object {
        const val PLANT_DETAILS_ARG_NAME = "origin"

        fun getTitleForRoute(route: String): String? {
            return when {
                route.startsWith(PlantDetails.route.substringBefore("/{")) -> PlantDetails.title
                route == PlantAPlant.route -> PlantAPlant.title
                route == BottomNavItem.Home.route -> BottomNavItem.Home.title
                route == BottomNavItem.TaskList.route -> BottomNavItem.TaskList.title
                route == BottomNavItem.Notifications.route -> BottomNavItem.Notifications.title
                else -> null
            }
        }
    }
}

open class BottomNavItem(route: String, title: String, val icon: ImageVector) :
    NavigationItem(route, title) {
    object Home : BottomNavItem("myGarden", "My Garden", Icons.Rounded.Home)
    object TaskList : BottomNavItem("taskList", "Tasks", Icons.Rounded.Done)
    object Notifications :
        BottomNavItem("notifications", "Notifications", Icons.Rounded.Notifications)
}

