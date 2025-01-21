package szysz3.planty.navigation.bottombar

import androidx.compose.ui.graphics.vector.ImageVector

open class NavigationItem(val route: String, val title: String)

/**
 * Navigation item specifically for bottom navigation bar entries.
 * Extends [NavigationItem] with additional icon and graph route handling.
 *
 * @property icon Vector icon to display in the bottom navigation bar
 * @property graphRoute The base route prefix for the navigation graph containing this item
 */
open class BottomNavItem(
    route: String,
    title: String,
    val icon: ImageVector,
    val graphRoute: String
) :
    NavigationItem(route, title) {

    fun isEntryPointForGivenRoute(route: String?): Boolean {
        route ?: return false
        return getEntryPoint(route) == getEntryPoint(routeWithoutGraphPrefix())
    }

    private fun routeWithoutGraphPrefix(): String {
        return route.removePrefix(graphRoute)
    }

    private fun getEntryPoint(route: String): String? {
        val regex = "/([^/?]+)".toRegex()
        val matchResult = regex.find(route)
        return matchResult?.groups?.get(1)?.value
    }
}