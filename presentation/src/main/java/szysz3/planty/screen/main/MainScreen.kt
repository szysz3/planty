package szysz3.planty.screen.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import szysz3.planty.screen.home.HomeScreenViewModel

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
            TopBar(mainScreenViewModel, homeScreenViewModel)
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