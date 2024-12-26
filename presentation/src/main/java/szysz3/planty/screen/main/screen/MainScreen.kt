package szysz3.planty.screen.main.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import szysz3.planty.screen.main.composable.BottomNavigationBar
import szysz3.planty.screen.main.composable.NavigationGraph
import szysz3.planty.screen.main.composable.TopBar
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.mygarden.viewmodel.MyGardenViewModel
import szysz3.planty.screen.plantcatalog.viewmodel.PlantCatalogViewModel

@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModel = hiltViewModel(),
    myGardenViewModel: MyGardenViewModel = hiltViewModel(),
    plantCatalogViewModel: PlantCatalogViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        bottomBar = { BottomNavigationBar(navController, mainScreenViewModel) },
        topBar = {
            TopBar(navController, mainScreenViewModel, myGardenViewModel)
        },
    ) { innerPadding ->
        NavigationGraph(
            navController = navController,
            mainScreenViewModel = mainScreenViewModel,
            myGardenViewModel = myGardenViewModel,
            plantCatalogViewModel = plantCatalogViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}