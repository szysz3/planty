package szysz3.planty.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import szysz3.planty.screen.dashboard.DashboardScreen
import szysz3.planty.screen.home.HomeScreen
import szysz3.planty.screen.notification.NotificationsScreen

@Composable
fun MainScreen(viewModel: MainScreenViewModel = hiltViewModel()) {
    val isTopBarVisible by viewModel.isTopBarVisible.collectAsState()
    val isActionButtonVisible by viewModel.isActionButtonVisible.collectAsState()
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController) },
        topBar = {
            TopBar(isTopBarVisible)
        },
        floatingActionButton = {
            if (isActionButtonVisible) {

            }
        }
    ) { innerPadding ->
        NavigationGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

// Define the BottomNavigation items
sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Home : BottomNavItem("home", Icons.Rounded.Home, "Home")
    object Dashboard : BottomNavItem("dashboard", Icons.Rounded.Person, "Dashboard")
    object Notifications :
        BottomNavItem("notifications", Icons.Rounded.Notifications, "Notifications")
}

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreen()
        }
        composable(BottomNavItem.Dashboard.route) {
            DashboardScreen()
        }
        composable(BottomNavItem.Notifications.route) {
            NotificationsScreen()
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(show: Boolean) {
    AnimatedVisibility(show) {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center // Center the text within the Box
                ) {
                    Text(
                        text = "Home",
                        maxLines = 1
                    )
                }
            },
            actions = {
                IconButton(onClick = {
//                isDeleteDialogVisible = true
                }) {
                    Icon(Icons.Rounded.Delete, contentDescription = "Delete")
                }
            },
        )
    }

}