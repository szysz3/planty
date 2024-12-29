package szysz3.planty.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController

open class NavigationItem(val route: String, val title: String)

open class BottomNavItem(route: String, title: String, val icon: ImageVector) :
    NavigationItem(route, title)

fun NavHostController.showScreen(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState = true
    }
}