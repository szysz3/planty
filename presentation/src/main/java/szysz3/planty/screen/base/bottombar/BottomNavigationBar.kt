package szysz3.planty.screen.base.bottombar

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import szysz3.planty.navigation.bottombar.BottomBarNavigationItems

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    val items = BottomBarNavigationItems.items
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val unselectedAlpha = 0.4f

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background,
    ) {
        items.forEach { item ->
            val isSelected =
                item.isEntryPointForGivenRoute(currentBackStackEntry?.destination?.route)
            BottomNavigationItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.title,
                        tint = if (isSelected) MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.onBackground.copy(
                            alpha = unselectedAlpha
                        )
                    )
                },
                label = {
                    Text(
                        item.title,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isSelected) MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.onBackground.copy(
                            alpha = unselectedAlpha
                        )
                    )
                },
                selected = isSelected,
                onClick = {
                    if (isSelected) {
                        return@BottomNavigationItem
                    }

                    navController.navigate(item.route) {
                        while (navController.popBackStack()) {
                            // clear backstack
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.secondary.copy(alpha = unselectedAlpha)
            )
        }
    }
}
