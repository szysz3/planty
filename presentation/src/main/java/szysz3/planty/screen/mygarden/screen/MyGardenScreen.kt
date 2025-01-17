package szysz3.planty.screen.mygarden.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyGardenScreen(
    title: String,
    navController: NavHostController,
    onPlantChosen: (plantId: Int, row: Int, column: Int, gardenId: Int) -> Unit,
    onEmptyGardenFieldChosen: (row: Int, column: Int, gardenId: Int) -> Unit,
    myGardenViewModel: MyGardenViewModel = hiltViewModel(),
) {
    val uiState by myGardenViewModel.uiState.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.navigationState.currentGardenId) {
        uiState.navigationState.currentGardenId?.let {
            myGardenViewModel.observeGardenState()
        }
    }

    LaunchedEffect(myGardenViewModel.uiEvent) {
        myGardenViewModel.uiEvent.collectLatest { event ->
            when (event) {
                is MyGardenScreenUiEvent.OnPlantChosen -> {
                    onPlantChosen(event.plant.id, event.row, event.column, event.gardenId)
                }

                is MyGardenScreenUiEvent.OnEmptyCellChosen -> {
                    onEmptyGardenFieldChosen(event.row, event.column, event.gardenId)
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                GardenBreadcrumb(
                    modifier = Modifier
                        .alpha(
                            if (uiState.navigationState.currentGardenPath.isNotEmpty()) {
                                1f
                            } else {
                                0f
                            }
                        )
                        .height(56.dp),
                    gardenPath = uiState.navigationState.currentGardenPath.map { it.id },
                    onNavigate = { gardenId ->
                        myGardenViewModel.navigateToGarden(gardenId)
                    }
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
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
                            gardenRows = uiState.gardenState.rows,
                            gardenColumns = uiState.gardenState.columns,
                            gardenState = uiState.gardenState,
                            isEditMode = uiState.editState.isEditMode,
                            selectedCellPositions = uiState.editState.selectedCells,
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
                        myGardenViewModel.createGardenFromDimensions("name", height, width)
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

            if (uiState.dialogState.showCreateSubGardenDialog) {
                GardenDimensionsInput(
                    bottomSheetState = bottomSheetState,
                    onDismissRequest = {
                        myGardenViewModel.showSubGardenDialog(false)
                    },
                    onDimensionsSubmitted = { height, width ->
                        myGardenViewModel.createSubGardenFromMergedCell("name", height, width)
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }.invokeOnCompletion {
                            if (!bottomSheetState.isVisible) {
                                myGardenViewModel.showSubGardenDialog(false)
                            }
                        }
                    }
                )
            }
        }
    }
}