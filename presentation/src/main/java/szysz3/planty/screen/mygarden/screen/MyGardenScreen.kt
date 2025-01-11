import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import szysz3.planty.core.composable.DeleteAlertDialog
import szysz3.planty.core.composable.FloatingActionButton
import szysz3.planty.screen.base.BaseScreen
import szysz3.planty.screen.base.topbar.TopBarDeleteButton
import szysz3.planty.screen.mygarden.composable.CreateSubGardenDialog
import szysz3.planty.screen.mygarden.composable.GardenBreadcrumb
import szysz3.planty.screen.mygarden.composable.GardenDimensionsInput
import szysz3.planty.screen.mygarden.composable.GardenEditToolbar
import szysz3.planty.screen.mygarden.composable.GardenMap
import szysz3.planty.screen.mygarden.viewmodel.MyGardenViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyGardenScreen(
    title: String,
    navController: NavHostController,
    onPlantChosen: (plantId: Int, row: Int, column: Int) -> Unit,
    onEmptyGardenFieldChosen: (row: Int, column: Int) -> Unit,
    viewModel: MyGardenViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.observeGardenState(uiState.currentGardenId)
    }

    BaseScreen(
        title = title,
        showTopBar = true,
        showBottomBar = true,
        topBarActions = {
            if (!uiState.isEditMode) {
                TopBarDeleteButton(
                    showDeleteButton = uiState.dataLoaded,
                    onDeleteClick = {
                        viewModel.showDeleteDialog(true)
                    }
                )
                IconButton(onClick = { viewModel.toggleEditMode() }) {
                    Icon(Icons.Default.Edit, "Enter edit mode")
                }
            }
        },
        navController = navController
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
//            GardenTransition(targetState = uiState.currentGardenId) {
            Column {
                if (uiState.currentGardenPath.isNotEmpty()) {
                    GardenBreadcrumb(
                        gardenPath = uiState.currentGardenPath,
                        onNavigate = { gardenId -> viewModel.navigateToGarden(gardenId) }
                    )
                }

                if (uiState.isEditMode) {
                    Timber.d("Edit mode active, showing toolbar with selection: ${uiState.selectedCells}")
                    GardenEditToolbar(
                        onConfirmMerge = {
                            Timber.d("Merge button clicked")
                            viewModel.mergeCells()
                        },
                        onCancelEdit = {
                            Timber.d("Cancel edit clicked")
                            viewModel.toggleEditMode()
                        },
                        isMergeEnabled = uiState.selectedCells.size >= 2 &&
                                viewModel.isValidRectangularSelection(uiState.selectedCells)
                    )
                }

                if (uiState.dataLoaded && uiState.gardenState.rows > 0 && uiState.gardenState.columns > 0) {
                    GardenMap(
                        rows = uiState.gardenState.rows,
                        columns = uiState.gardenState.columns,
                        state = uiState.gardenState,
                        isEditMode = uiState.isEditMode,
                        selectedCells = uiState.selectedCells,
                        onCellClick = { row, col -> viewModel.onCellClick(row, col) },
                        onMergedCellClick = { mergedCell ->
                            viewModel.onMergedCellClick(
                                mergedCell
                            )
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        FloatingActionButton(
                            icon = Icons.Rounded.Add,
                            onClick = { viewModel.showBottomSheet(true) }
                        )
                    }
                }
            }
//            }

            if (uiState.isDeleteDialogVisible) {
                DeleteAlertDialog(
                    title = "Delete Garden",
                    message = "Are you sure you want to delete this garden?",
                    confirmButtonText = "Delete",
                    dismissButtonText = "Cancel",
                    onConfirmDelete = {
                        viewModel.clearGarden()
                        viewModel.showDeleteDialog(false)
                    },
                    onCancel = {
                        viewModel.showDeleteDialog(false)
                    }
                )
            }

            if (uiState.isBottomSheetVisible) {
                GardenDimensionsInput(
                    bottomSheetState = bottomSheetState,
                    onDismissRequest = { viewModel.showBottomSheet(false) },
                    onDimensionsSubmitted = { height, width ->
                        viewModel.createGarden(height, width)
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }.invokeOnCompletion {
                            if (!bottomSheetState.isVisible) viewModel.showBottomSheet(false)
                        }
                    }
                )
            }

            if (uiState.showCreateSubGardenDialog) {
                CreateSubGardenDialog(
                    onDismiss = { viewModel.hideCreateSubGardenDialog() },
                    onConfirm = { rows, columns -> viewModel.createSubGarden(rows, columns) }
                )
            }
        }
    }
}