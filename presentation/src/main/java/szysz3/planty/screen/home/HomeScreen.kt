package szysz3.planty.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import szysz3.planty.R
import szysz3.planty.screen.main.MainScreenViewModel
import szysz3.planty.ui.widgets.RoundedButton
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
    mainScreenViewModel: MainScreenViewModel = hiltViewModel()
) {
    val gardenDimensions by homeScreenViewModel.gardenDimensions.collectAsState()
    val isDeleteDialogVisible by homeScreenViewModel.isDeleteDialogVisible.collectAsState()
    val isBottomSheetVisible by homeScreenViewModel.isBottomSheetVisible.collectAsState()

    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (gardenDimensions != null) {
            GardenMap(
                rows = gardenDimensions?.rowCount ?: 0,
                columns = gardenDimensions?.columnCount ?: 0,
                plants = listOf("A", "B", "C")
            ) { row, col, plant ->
                Timber.i("GardenMap: $row, $col, $plant")
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.placeholder),
                    contentDescription = "Garden map placeholder",
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                RoundedButton(
                    onClick = { homeScreenViewModel.showBottomSheet(true) },
                    text = "Create New Map",
                )
            }
        }
    }

    if (isDeleteDialogVisible) {
        AlertDialog(
            onDismissRequest = { homeScreenViewModel.showDeleteDialog(false) },
            title = { Text(text = "Confirm Delete") },
            text = { Text(text = "Are you sure you want to delete your garden map?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // TODO: perform delete action here
                        homeScreenViewModel.setGardenDimensions(MapDimensions(0, 0))
                        mainScreenViewModel.showTopBar(false)
                        homeScreenViewModel.showDeleteDialog(false)
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    homeScreenViewModel.showDeleteDialog(false)
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (isBottomSheetVisible) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            modifier = Modifier
                .fillMaxSize(),
            onDismissRequest = { homeScreenViewModel.showBottomSheet(false) }
        ) {
            Box(
                modifier = Modifier.wrapContentHeight()
            ) {
                DimensionsInput(
                    onDimensionsSubmitted = { dimensions ->
                        homeScreenViewModel.setGardenDimensions(dimensions)
                        mainScreenViewModel.showTopBar(true)
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }.invokeOnCompletion {
                            if (!bottomSheetState.isVisible) {
                                homeScreenViewModel.showBottomSheet(false)
                            }
                        }
                    }
                )
            }
        }
    }
}