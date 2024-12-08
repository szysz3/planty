import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import szysz3.planty.R
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.mygarden.composable.GardenDimensionsInput
import szysz3.planty.screen.mygarden.composable.GardenMap
import szysz3.planty.screen.mygarden.viewmodel.MyGardenViewModel
import szysz3.planty.screen.plantdetails.model.PlantDetailsScreenOrigin
import szysz3.planty.ui.widgets.DeleteAlertDialog
import szysz3.planty.ui.widgets.EllipticalBackground
import szysz3.planty.ui.widgets.FloatingActionButton

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyGardenScreen(
    mainScreenViewModel: MainScreenViewModel,
    myGardenViewModel: MyGardenViewModel,
    onNavigateToPlantAPlant: () -> Unit,
    onNavigateToPlantDetails: (origin: PlantDetailsScreenOrigin) -> Unit
) {
    val uiState by myGardenViewModel.uiState.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        mainScreenViewModel.updateShowBackButton(false)
        myGardenViewModel.loadGarden()
    }

    LaunchedEffect(uiState.dataLoaded) {
        mainScreenViewModel.updateHomeScreenInitialized(uiState.dataLoaded)
        mainScreenViewModel.updateTopBarVisibility(uiState.dataLoaded)
        mainScreenViewModel.updateShowDeleteButton(uiState.dataLoaded)
    }

    val rows = uiState.gardenState.rows
    val columns = uiState.gardenState.columns

    EllipticalBackground(R.drawable.bcg1)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.dataLoaded && rows > 0 && columns > 0) {
            GardenMap(
                rows = rows,
                columns = columns,
                state = uiState.gardenState,
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
            FloatingActionButton(
                icon = Icons.Rounded.Add,
                contentDescription = "Add garden",
                onClick = {
                    myGardenViewModel.showBottomSheet(true)
                })
        }
    }

    if (uiState.isDeleteDialogVisible) {
        DeleteAlertDialog(
            title = "Delete Garden",
            message = "Are you sure you want to delete this garden?",
            confirmButtonText = "Delete",
            dismissButtonText = "Cancel",
            onConfirmDelete = {
                myGardenViewModel.clearGarden()
                myGardenViewModel.showDeleteDialog(false)
            },
            onCancel = {
                myGardenViewModel.showDeleteDialog(false)
            }
        )
    }

    if (uiState.isBottomSheetVisible) {
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