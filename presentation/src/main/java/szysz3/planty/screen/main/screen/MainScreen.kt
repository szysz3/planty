package szysz3.planty.screen.main.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import szysz3.planty.screen.home.viewmodel.HomeScreenViewModel
import szysz3.planty.screen.main.composable.BottomNavigationBar
import szysz3.planty.screen.main.composable.NavigationGraph
import szysz3.planty.screen.main.composable.TopBar
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel

@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModel = hiltViewModel(),
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController, mainScreenViewModel) },
        topBar = {
            TopBar(navController, mainScreenViewModel, homeScreenViewModel)
        },
    ) { innerPadding ->
        NavigationGraph(
            navController = navController,
            mainScreenViewModel = mainScreenViewModel,
            homeScreenViewModel = homeScreenViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}