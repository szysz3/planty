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
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.mygarden.composable.DeleteGardenDialog
import szysz3.planty.screen.mygarden.composable.EmptyGardenPlaceholder
import szysz3.planty.screen.mygarden.composable.GardenDimensionsInput
import szysz3.planty.screen.mygarden.composable.GardenMap
import szysz3.planty.screen.mygarden.viewmodel.MyGardenViewModel
import szysz3.planty.screen.plantdetails.screen.PlantDetailsScreenOrigin

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyGardenScreen(
    mainScreenViewModel: MainScreenViewModel,
    myGardenViewModel: MyGardenViewModel,
    onNavigateToPlantAPlant: () -> Unit,
    onNavigateToPlantDetails: (origin: PlantDetailsScreenOrigin) -> Unit
) {
    val gardenState by myGardenViewModel.gardenState.collectAsState()
    val isDeleteDialogVisible by myGardenViewModel.isDeleteDialogVisible.collectAsState()
    val isBottomSheetVisible by myGardenViewModel.isBottomSheetVisible.collectAsState()
    val dataLoaded by myGardenViewModel.dataLoaded.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        mainScreenViewModel.showBackButton(false)
        myGardenViewModel.loadGarden()
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
                state = gardenState,
                onPlantSelected = { row, col ->
                    myGardenViewModel.updateSelectedCell(row, col)
                    if (myGardenViewModel.getPlantForSelectedCell() == null) {
                        onNavigateToPlantAPlant()
                    } else {
                        onNavigateToPlantDetails(PlantDetailsScreenOrigin.HOME_SCREEN)
                    }
                }
            )
        } else {
            EmptyGardenPlaceholder(onCreateNewMap = {
                myGardenViewModel.showBottomSheet(true)
            })
        }
    }

    if (isDeleteDialogVisible) {
        DeleteGardenDialog(
            onConfirmDelete = {
                myGardenViewModel.clearGarden()
                myGardenViewModel.showDeleteDialog(false)
            },
            onCancel = {
                myGardenViewModel.showDeleteDialog(false)
            }
        )
    }

    if (isBottomSheetVisible) {
        GardenDimensionsInput(
            bottomSheetState = bottomSheetState,
            onDismissRequest = { myGardenViewModel.showBottomSheet(false) },
            onDimensionsSubmitted = { height, width ->
                myGardenViewModel.initializeGarden(height, width)
                coroutineScope.launch {
                    bottomSheetState.hide()
                }.invokeOnCompletion {
                    if (!bottomSheetState.isVisible) myGardenViewModel.showBottomSheet(false)
                }
            }
        )
    }
}
