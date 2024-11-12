package szysz3.planty.screen.main.composable

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    mainScreenViewModel: MainScreenViewModel,
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Dashboard,
        BottomNavItem.Notifications
    )
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.surface, // Theme's surface color for navigation background
        contentColor = MaterialTheme.colorScheme.onSurface,  // Theme's color for icons and text
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            BottomNavigationItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.title,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.6f
                        ) // Primary color for selected
                    )
                },
                label = {
                    Text(
                        item.title,
                        style = MaterialTheme.typography.labelLarge, // Using theme's label style
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.6f
                        )
                    )
                },
                selected = isSelected,
                onClick = {
                    mainScreenViewModel.handleTopBarVisibility(item.route)

                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                selectedContentColor = MaterialTheme.colorScheme.primary, // Selected item color
                unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) // Unselected item color with reduced opacity
            )
        }
    }
}