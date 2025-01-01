package szysz3.planty.navigation.bottombar

import androidx.compose.ui.graphics.vector.ImageVector

open class NavigationItem(val route: String, val title: String)

open class BottomNavItem(
    route: String,
    title: String,
    val icon: ImageVector,
    val graphRoute: String
) :
    NavigationItem(route, title) {
    fun routeWithoutGraph(): String {
        return route.removePrefix(graphRoute)
    }
}