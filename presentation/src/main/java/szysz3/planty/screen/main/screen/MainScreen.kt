package szysz3.planty.screen.main.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import szysz3.planty.navigation.NavigationGraph

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavigationGraph(navController = navController)
}