package szysz3.planty.screen.main.composable

import HomeScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import szysz3.planty.screen.dashboard.DashboardScreen
import szysz3.planty.screen.home.viewmodel.HomeScreenViewModel
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.notification.NotificationsScreen
import szysz3.planty.screen.plantaplant.screen.PlantAPlantScreen
import szysz3.planty.screen.plantaplant.viewmodel.PlantAPlantViewModel
import szysz3.planty.screen.plantdetails.screen.PlantDetailsScreen
import szysz3.planty.screen.plantdetails.screen.PlantDetailsScreenOrigin

@Composable
fun NavigationGraph(
    navController: NavHostController,
    mainScreenViewModel: MainScreenViewModel,
    homeScreenViewModel: HomeScreenViewModel,
    plantAPlantViewModel: PlantAPlantViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreen(
                mainScreenViewModel = mainScreenViewModel,
                homeScreenViewModel = homeScreenViewModel,
                onNavigateToPlantAPlant = {
                    navigate(navController, NavigationItem.PlantAPlant)
                },
                onNavigateToPlantDetails = { origin ->
                    navigate(navController, NavigationItem.PlantDetails.withArgs(origin.value))
                }
            )
        }
        composable(BottomNavItem.Dashboard.route) {
            DashboardScreen()
        }
        composable(BottomNavItem.Notifications.route) {
            NotificationsScreen()
        }
        composable(
            route = NavigationItem.PlantDetails.route,
            arguments = listOf(
                navArgument(NavigationItem.plantDetailsScreenArgName) {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val origin =
                backStackEntry.arguments?.getInt(NavigationItem.plantDetailsScreenArgName) ?: 0
            PlantDetailsScreen(
                mainScreenViewModel = mainScreenViewModel,
                plantAPlantViewModel = plantAPlantViewModel,
                homeScreenViewModel = homeScreenViewModel,
                origin = PlantDetailsScreenOrigin.fromValue(origin)
            ) {
                navController.popBackStack(BottomNavItem.Home.route, false)
            }
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

open class NavigationItem(val route: String) {
    object PlantAPlant : NavigationItem("\\home\\plantAPlant")
    object PlantDetails : NavigationItem("\\home\\plantDetails/{${plantDetailsScreenArgName}}") {
        fun withArgs(origin: Int): NavigationItem {
            return NavigationItem(
                route.replace(
                    "{${plantDetailsScreenArgName}}",
                    origin.toString()
                )
            )
        }
    }

    companion object {
        const val plantDetailsScreenArgName = "origin"
    }
}

open class BottomNavItem(route: String, val icon: ImageVector, val title: String) :
    NavigationItem(route) {
    object Home : BottomNavItem("home", Icons.Rounded.Home, "Home")
    object Dashboard : BottomNavItem("dashboard", Icons.Rounded.Person, "Dashboard")
    object Notifications :
        BottomNavItem("notifications", Icons.Rounded.Notifications, "Notifications")
}