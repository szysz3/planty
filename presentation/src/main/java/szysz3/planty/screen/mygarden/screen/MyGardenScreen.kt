import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import szysz3.planty.R
import szysz3.planty.core.composable.DeleteAlertDialog
import szysz3.planty.core.composable.EllipticalBackground
import szysz3.planty.core.composable.FloatingActionButton
import szysz3.planty.screen.base.BaseScreen
import szysz3.planty.screen.base.topbar.TopBarDeleteButton
import szysz3.planty.screen.mygarden.composable.GardenBreadcrumb
import szysz3.planty.screen.mygarden.composable.GardenDimensionsInput
import szysz3.planty.screen.mygarden.composable.GardenEditToolbar
import szysz3.planty.screen.mygarden.composable.GardenMap
import szysz3.planty.screen.mygarden.model.MyGardenScreenUiEvent
import szysz3.planty.screen.mygarden.viewmodel.MyGardenViewModel
import timber.log.Timber

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

    LaunchedEffect(myGardenViewModel.uiEvent) {
        myGardenViewModel.uiEvent.collectLatest { event ->
            when (event) {
                is MyGardenScreenUiEvent.OnPlantChosen -> {
                    onPlantChosen(event.plant.id, event.row, event.column)
                }

                is MyGardenScreenUiEvent.OnEmptyCellChosen -> {
                    onEmptyGardenFieldChosen(event.row, event.column)
                }
            }
        }
    }

    BaseScreen(
        title = title,
        showTopBar = true,
        showBottomBar = true,
        topBarActions = {
            if (uiState.dataLoaded && !uiState.editState.isEditMode) {
                IconButton(onClick = { myGardenViewModel.toggleEditMode() }) {
                    Icon(Icons.Default.Edit, "Enter edit mode")
                }
            }

            TopBarDeleteButton(
                showDeleteButton = uiState.dataLoaded,
                onDeleteClick = {
                    myGardenViewModel.showDeleteDialog(true)
                }
            )
        },
        navController = navController
    ) { padding ->
        EllipticalBackground(R.drawable.bcg1)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (uiState.navigationState.currentGardenPath.isNotEmpty()) {
                    GardenBreadcrumb(
                        gardenPath = uiState.navigationState.currentGardenPath,
                        onNavigate = {
                            // TODO: Implement navigation
                        }
                    )
                }

                GardenEditToolbar(
                    modifier = Modifier.alpha(
                        if (uiState.editState.isEditMode) 1f else 0f
                    ),
                    onConfirmMerge = {
                        Timber.d("Merge button clicked")
                        myGardenViewModel.mergeCells()
                    },
                    onCancelEdit = {
                        Timber.d("Cancel edit clicked")
                        myGardenViewModel.toggleEditMode()
                    },
                    isMergeEnabled = uiState.editState.selectedCells.size >= 2 &&
                            myGardenViewModel.isValidRectangularSelection()
                )

                if (uiState.dataLoaded && uiState.gardenState.rows > 0 && uiState.gardenState.columns > 0) {
                    GardenMap(
                        rows = uiState.gardenState.rows,
                        columns = uiState.gardenState.columns,
                        state = uiState.gardenState,
                        isEditMode = uiState.editState.isEditMode,
                        selectedCells = uiState.editState.selectedCells,
                        onCellClick = { row, col ->
                            myGardenViewModel.onCellClick(row, col)
                        },
                        onMergedCellClick = { mergedCell ->
                            myGardenViewModel.onMergedCellClick(mergedCell)
                        }
                    )
                } else {
                    FloatingActionButton(
                        icon = Icons.Rounded.Add,
                        contentDescription = "Add garden",
                        onClick = {
                            myGardenViewModel.showBottomSheet(true)
                        }
                    )
                }
            }

            if (uiState.dialogState.isDeleteDialogVisible) {
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

            if (uiState.dialogState.isBottomSheetVisible) {
                GardenDimensionsInput(
                    bottomSheetState = bottomSheetState,
                    onDismissRequest = {
                        myGardenViewModel.showBottomSheet(false)
                    },
                    onDimensionsSubmitted = { height, width ->
                        myGardenViewModel.createGarden(height, width)
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }.invokeOnCompletion {
                            if (!bottomSheetState.isVisible) {
                                myGardenViewModel.showBottomSheet(false)
                            }
                        }
                    }
                )
            }
        }
    }
}