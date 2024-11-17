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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import szysz3.planty.screen.dashboard.DashboardScreen
import szysz3.planty.screen.home.viewmodel.HomeScreenViewModel
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.notification.NotificationsScreen
import szysz3.planty.screen.plantaplant.screen.PlantAPlantScreen
import szysz3.planty.screen.plantaplant.viewmodel.PlantAPlantViewModel
import szysz3.planty.screen.plantdetails.screen.PlantDetailsScreen

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
                onNavigateToPlantDetails = {
                    navigate(navController, NavigationItem.PlantDetails)
                }
            )
        }
        composable(BottomNavItem.Dashboard.route) {
            DashboardScreen()
        }
        composable(BottomNavItem.Notifications.route) {
            NotificationsScreen()
        }
        composable(NavigationItem.PlantDetails.route) {
            PlantDetailsScreen(
                mainScreenViewModel = mainScreenViewModel,
                plantAPlantViewModel = plantAPlantViewModel
            ) {
                navController.popBackStack(BottomNavItem.Home.route, false)
            }
        }
        composable(NavigationItem.PlantAPlant.route) {
            PlantAPlantScreen(
                mainScreenViewModel = mainScreenViewModel,
                plantAPlantViewModel = plantAPlantViewModel
            ) {
                navigate(navController, NavigationItem.PlantDetails)
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
    object PlantDetails : NavigationItem("\\home\\plantDetails")
}

open class BottomNavItem(route: String, val icon: ImageVector, val title: String) :
    NavigationItem(route) {
    object Home : BottomNavItem("home", Icons.Rounded.Home, "Home")
    object Dashboard : BottomNavItem("dashboard", Icons.Rounded.Person, "Dashboard")
    object Notifications :
        BottomNavItem("notifications", Icons.Rounded.Notifications, "Notifications")
}