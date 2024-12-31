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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import szysz3.planty.R
import szysz3.planty.core.composable.DeleteAlertDialog
import szysz3.planty.core.composable.EllipticalBackground
import szysz3.planty.core.composable.FloatingActionButton
import szysz3.planty.screen.base.BaseScreen
import szysz3.planty.screen.mygarden.composable.GardenDimensionsInput
import szysz3.planty.screen.mygarden.composable.GardenMap
import szysz3.planty.screen.mygarden.viewmodel.MyGardenViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyGardenScreen(
    title: String,
    navController: NavHostController,
    onPlantChosen: (plantId: Int, row: Int, column: Int) -> Unit,
    onEmptyGardenFieldChosen: (row: Int, column: Int) -> Unit,
    myGardenViewModel: MyGardenViewModel = hiltViewModel(),
) {
    val uiState by myGardenViewModel.uiState.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        myGardenViewModel.observeGardenState()
    }

    BaseScreen(
        title = title,
        showTopBar = true,
        showBottomBar = true,
        navController = navController
    ) { padding ->
        EllipticalBackground(R.drawable.bcg1)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.dataLoaded
                && uiState.gardenState.rows > 0
                && uiState.gardenState.columns > 0
            ) {
                GardenMap(
                    rows = uiState.gardenState.rows,
                    columns = uiState.gardenState.columns,
                    state = uiState.gardenState,
                    onPlantSelected = { row, col ->
                        myGardenViewModel.updateSelectedCell(row, col)
                        val plantForSelectedCell = myGardenViewModel.getPlantForSelectedCell()
                        if (plantForSelectedCell == null) {
                            onEmptyGardenFieldChosen(row, col)
                        } else {
                            onPlantChosen(
                                plantForSelectedCell.id,
                                row,
                                col
                            )
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
                    myGardenViewModel.createGarden(height, width)
                    coroutineScope.launch {
                        bottomSheetState.hide()
                    }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) myGardenViewModel.showBottomSheet(false)
                    }
                }
            )
        }
    }
}