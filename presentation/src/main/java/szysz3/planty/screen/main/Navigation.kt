package szysz3.planty.screen.main

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
import szysz3.planty.screen.home.HomeScreen
import szysz3.planty.screen.home.HomeScreenViewModel
import szysz3.planty.screen.notification.NotificationsScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    mainScreenViewModel: MainScreenViewModel,
    homeScreenViewModel: HomeScreenViewModel,
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
                homeScreenViewModel = homeScreenViewModel
            )
        }
        composable(BottomNavItem.Dashboard.route) {
            DashboardScreen()
        }
        composable(BottomNavItem.Notifications.route) {
            NotificationsScreen()
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Home : BottomNavItem("home", Icons.Rounded.Home, "Home")
    object Dashboard : BottomNavItem("dashboard", Icons.Rounded.Person, "Dashboard")
    object Notifications :
        BottomNavItem("notifications", Icons.Rounded.Notifications, "Notifications")
}