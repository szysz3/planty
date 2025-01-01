package szysz3.planty.screen.main.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import szysz3.planty.navigation.graph.MainNavigationGraph

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    MainNavigationGraph(navController = navController)
}