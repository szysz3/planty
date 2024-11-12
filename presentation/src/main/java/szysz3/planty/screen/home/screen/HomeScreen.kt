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
    homeScreenViewModel: HomeScreenViewModel
) {
    val gardenState by homeScreenViewModel.gardenState.collectAsState()
    val isDeleteDialogVisible by homeScreenViewModel.isDeleteDialogVisible.collectAsState()
    val isBottomSheetVisible by homeScreenViewModel.isBottomSheetVisible.collectAsState()
    val dataLoaded by homeScreenViewModel.dataLoaded.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        homeScreenViewModel.loadGarden()
    }

    LaunchedEffect(dataLoaded) {
        mainScreenViewModel.homeScreenInitialized(dataLoaded)
        mainScreenViewModel.showTopBar(dataLoaded)
    }

    val rowCount = gardenState.dimensions?.rowCount ?: 0
    val columnCount = gardenState.dimensions?.columnCount ?: 0

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (gardenState.cells != null && rowCount > 0 && columnCount > 0) {
            GardenMap(
                rows = rowCount,
                columns = columnCount,
                plants = listOf("A", "B", "C"),
                selectedCells = gardenState.cells ?: emptyList(),
                onPlantSelected = { row, col, plant ->
                    homeScreenViewModel.saveCell(row, col, plant)
                    Timber.i("GardenMap: $row, $col, $plant")
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
            onDimensionsSubmitted = { dimensions ->
                homeScreenViewModel.initializeGarden(dimensions)
                coroutineScope.launch {
                    bottomSheetState.hide()
                }.invokeOnCompletion {
                    if (!bottomSheetState.isVisible) homeScreenViewModel.showBottomSheet(false)
                }
            }
        )
    }
}
