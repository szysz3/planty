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
        BottomNavItem.TaskList,
        BottomNavItem.Catalog,
        BottomNavItem.PlantId
    )
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background,
    ) {
        items.forEach { item ->
            val isSelected = currentRoute?.contains(item.route) == true

            BottomNavigationItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.title,
                        tint = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.4f
                        ) // Primary color for selected
                    )
                },
                label = {
                    Text(
                        item.title,
                        style = MaterialTheme.typography.labelLarge, // Using theme's label style
                        color = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.4f
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
                unselectedContentColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f) // Unselected item color with reduced opacity
            )
        }
    }
}