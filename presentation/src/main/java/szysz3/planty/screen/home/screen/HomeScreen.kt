import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import szysz3.planty.screen.home.composable.DeleteGardenDialog
import szysz3.planty.screen.home.composable.EmptyGardenPlaceholder
import szysz3.planty.screen.home.composable.GardenDimensionsInput
import szysz3.planty.screen.home.composable.GardenMap
import szysz3.planty.screen.home.viewmodel.HomeScreenViewModel
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    mainScreenViewModel: MainScreenViewModel,
    homeScreenViewModel: HomeScreenViewModel,
    onNavigateToPlantAPlant: () -> Unit,
    onNavigateToPlantDetails: () -> Unit
) {
    val gardenState by homeScreenViewModel.gardenState.collectAsState()
    val isDeleteDialogVisible by homeScreenViewModel.isDeleteDialogVisible.collectAsState()
    val isBottomSheetVisible by homeScreenViewModel.isBottomSheetVisible.collectAsState()
    val dataLoaded by homeScreenViewModel.dataLoaded.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        mainScreenViewModel.showBackButton(false)
        homeScreenViewModel.loadGarden()
    }

    LaunchedEffect(dataLoaded) {
        mainScreenViewModel.homeScreenInitialized(dataLoaded)
        mainScreenViewModel.showTopBar(dataLoaded)
        mainScreenViewModel.showDeleteButton(dataLoaded)
    }

    val rows = gardenState.rows
    val columns = gardenState.columns

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (dataLoaded && rows > 0 && columns > 0) {
            GardenMap(
                rows = rows,
                columns = columns,
                plants = listOf("A", "B", "C"),
                selectedCells = gardenState.cells,
                onPlantSelected = { row, col, plant ->
                    // TODO: Handle plant selection in PlantAPlantScreen
//                    homeScreenViewModel.saveCell(row, col, plant)
                    Timber.i("GardenMap: $row, $col, $plant")
                    if (plant.isBlank()) {
                        onNavigateToPlantAPlant()
                    } else {
                        onNavigateToPlantDetails()
                    }
                }
            )
        } else {
            EmptyGardenPlaceholder(onCreateNewMap = {
                homeScreenViewModel.showBottomSheet(true)
            })
        }
    }

    if (isDeleteDialogVisible) {
        DeleteGardenDialog(
            onConfirmDelete = {
                homeScreenViewModel.clearGarden()
                homeScreenViewModel.showDeleteDialog(false)
            },
            onCancel = {
                homeScreenViewModel.showDeleteDialog(false)
            }
        )
    }

    if (isBottomSheetVisible) {
        GardenDimensionsInput(
            bottomSheetState = bottomSheetState,
            onDismissRequest = { homeScreenViewModel.showBottomSheet(false) },
            onDimensionsSubmitted = { height, width ->
                homeScreenViewModel.initializeGarden(height, width)
                coroutineScope.launch {
                    bottomSheetState.hide()
                }.invokeOnCompletion {
                    if (!bottomSheetState.isVisible) homeScreenViewModel.showBottomSheet(false)
                }
            }
        )
    }
}
