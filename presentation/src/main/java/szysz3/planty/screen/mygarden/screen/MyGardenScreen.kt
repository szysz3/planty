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
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import szysz3.planty.R
import szysz3.planty.core.composable.DeleteAlertDialog
import szysz3.planty.core.composable.EllipticalBackground
import szysz3.planty.core.composable.FloatingActionButton
import szysz3.planty.screen.base.BaseScreen
import szysz3.planty.screen.base.topbar.TopBarActionButton
import szysz3.planty.screen.mygarden.composable.GardenBreadcrumb
import szysz3.planty.screen.mygarden.composable.GardenEditToolbar
import szysz3.planty.screen.mygarden.composable.input.GardenDimensionsInput
import szysz3.planty.screen.mygarden.composable.map.GardenMap
import szysz3.planty.screen.mygarden.composable.map.GardenMapCallbacks
import szysz3.planty.screen.mygarden.composable.map.toGardenMapState
import szysz3.planty.screen.mygarden.model.MyGardenScreenState
import szysz3.planty.screen.mygarden.model.MyGardenScreenUiEvent
import szysz3.planty.screen.mygarden.viewmodel.MyGardenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyGardenScreen(
    title: String,
    navController: NavHostController,
    onPlantChosen: (plantId: Int, row: Int, column: Int, gardenId: Int) -> Unit,
    onEmptyGardenFieldChosen: (row: Int, column: Int, gardenId: Int) -> Unit,
    viewModel: MyGardenViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.navigationState.currentGardenId) {
        uiState.navigationState.currentGardenId?.let {
            viewModel.observeGardenState()
        }
    }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collectLatest { event ->
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
            MyGardenTopBarActions(
                dataLoaded = uiState.dataLoaded,
                isEditMode = uiState.editState.isEditMode,
                onEditClick = viewModel::toggleEditMode,
                onDeleteClick = { viewModel.showDeleteDialog(true) }
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
            MyGardenContent(
                uiState = uiState,
                viewModel = viewModel
            )

            MyGardenDialogs(
                uiState = uiState,
                bottomSheetState = bottomSheetState,
                coroutineScope = coroutineScope,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun MyGardenContent(
    uiState: MyGardenScreenState,
    viewModel: MyGardenViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        GardenBreadcrumb(
            modifier = Modifier.height(56.dp),
            gardenPath = uiState.navigationState.currentGardenPath,
            onNavigate = viewModel::navigateToGarden,
            isVisible = uiState.navigationState.currentGardenPath.isNotEmpty()
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GardenEditToolbar(
                modifier = Modifier,
                isVisible = uiState.editState.isEditMode,
                onConfirmMerge = viewModel::mergeCells,
                onCancelEdit = viewModel::toggleEditMode,
                isMergeEnabled = uiState.editState.selectedCells.size >= 2 &&
                        viewModel.isValidRectangularSelection()
            )

            if (uiState.dataLoaded && uiState.gardenState.rows > 0 && uiState.gardenState.columns > 0) {
                GardenMap(
                    state = uiState.gardenState.toGardenMapState(
                        isEditMode = uiState.editState.isEditMode,
                        selectedCells = uiState.editState.selectedCells
                    ),
                    callbacks = GardenMapCallbacks(
                        onCellClick = viewModel::onCellClick,
                        onMergedCellClick = viewModel::onMergedCellClick
                    )
                )
            } else {
                FloatingActionButton(
                    icon = Icons.Rounded.Add,
                    contentDescription = "Add garden",
                    onClick = { viewModel.showBottomSheet(true) }
                )
            }
        }
    }
}

@Composable
private fun MyGardenTopBarActions(
    dataLoaded: Boolean,
    isEditMode: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    if (dataLoaded && !isEditMode) {
        TopBarActionButton(
            icon = Icons.Default.Edit,
            showButton = true,
            onAction = onEditClick
        )
    } else if (dataLoaded) {
        TopBarActionButton(
            showButton = true,
            onAction = onDeleteClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyGardenDialogs(
    uiState: MyGardenScreenState,
    bottomSheetState: SheetState,
    coroutineScope: CoroutineScope,
    viewModel: MyGardenViewModel
) {
    if (uiState.dialogState.isDeleteDialogVisible) {
        DeleteAlertDialog(
            title = "Delete Garden",
            message = "Are you sure you want to delete this garden?",
            confirmButtonText = "Delete",
            dismissButtonText = "Cancel",
            onConfirmDelete = {
                viewModel.clearGarden()
                viewModel.showDeleteDialog(false)
            },
            onCancel = { viewModel.showDeleteDialog(false) }
        )
    }

    if (uiState.dialogState.isBottomSheetVisible) {
        GardenDimensionsInput(
            bottomSheetState = bottomSheetState,
            onDismissRequest = { viewModel.showBottomSheet(false) },
            onDimensionsSubmitted = { gardenName, height, width ->
                viewModel.createGardenFromDimensions(gardenName, height, width)
                coroutineScope.launch {
                    bottomSheetState.hide()
                }.invokeOnCompletion {
                    if (!bottomSheetState.isVisible) {
                        viewModel.showBottomSheet(false)
                    }
                }
            }
        )
    }

    if (uiState.dialogState.showCreateSubGardenDialog) {
        GardenDimensionsInput(
            bottomSheetState = bottomSheetState,
            onDismissRequest = { viewModel.showSubGardenDialog(false) },
            onDimensionsSubmitted = { gardenName, height, width ->
                viewModel.createSubGardenFromMergedCell(gardenName, height, width)
                coroutineScope.launch {
                    bottomSheetState.hide()
                }.invokeOnCompletion {
                    if (!bottomSheetState.isVisible) {
                        viewModel.showSubGardenDialog(false)
                    }
                }
            }
        )
    }
}