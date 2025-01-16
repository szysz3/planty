package szysz3.planty.screen.plantdetails.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import szysz3.planty.R
import szysz3.planty.core.composable.DeleteAlertDialog
import szysz3.planty.core.composable.EllipticalBackground
import szysz3.planty.core.composable.RoundedButton
import szysz3.planty.core.model.Plant
import szysz3.planty.core.model.PlantDetailsConfig
import szysz3.planty.core.util.openWebSearch
import szysz3.planty.screen.base.BaseScreen
import szysz3.planty.screen.base.topbar.TopBarBackButton
import szysz3.planty.screen.base.topbar.TopBarDeleteButton
import szysz3.planty.screen.plantdetails.composable.CultivationDetailsSection
import szysz3.planty.screen.plantdetails.composable.PlantDetailsGrid
import szysz3.planty.screen.plantdetails.composable.PlantImageSection
import szysz3.planty.screen.plantdetails.composable.PlantNameSection
import szysz3.planty.screen.plantdetails.composable.WebSearchButton
import szysz3.planty.screen.plantdetails.viewmodel.PlantDetailsViewModel

@Composable
fun PlantDetailsScreen(
    title: String,
    navController: NavHostController,
    config: PlantDetailsConfig,
    plantId: Int,
    row: Int?,
    column: Int?,
    gardenId: Int?,
    onPlantChosen: (() -> Unit)?,
    onPlantImageClicked: (plantId: Int) -> Unit,
    plantDetailsViewModel: PlantDetailsViewModel = hiltViewModel(),
) {
    val uiState by plantDetailsViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(plantId) {
        plantDetailsViewModel.initialize(config, plantId, row, column, gardenId)
    }

    BaseScreen(
        title = title,
        showTopBar = true,
        showBottomBar = true,
        topBarActions = {
            TopBarDeleteButton(
                showDeleteButton = uiState.isDeleteButtonVisible,
                onDeleteClick = { plantDetailsViewModel.showDeleteDialog(true) }
            )
        },
        topBarBackNavigation = {
            TopBarBackButton(
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        },
        navController = navController
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            EllipticalBackground(
                backgroundImageId = R.drawable.bcg3,
                tintLevelCenter = 0.5f
            )

            uiState.selectedPlant?.let { plant ->
                PlantDetailsContent(
                    plant = plant,
                    onPlantImageClicked = onPlantImageClicked,
                    onPlantChosen = {
                        plantDetailsViewModel.persistPlant(plant)
                        onPlantChosen?.invoke()
                    },
                    isPlantButtonVisible = uiState.isPlantButtonVisible,
                    onWebSearch = { openWebSearch(plant.latinName, context) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                )
            }

            if (uiState.isDeleteDialogVisible) {
                DeleteAlertDialog(
                    title = "Delete Plant",
                    message = "Are you sure you want to delete this plant?",
                    confirmButtonText = "Delete",
                    dismissButtonText = "Cancel",
                    onConfirmDelete = {
                        plantDetailsViewModel.persistPlant(plant = null)
                        plantDetailsViewModel.showDeleteDialog(false)
                        navController.popBackStack()
                    },
                    onCancel = { plantDetailsViewModel.showDeleteDialog(false) }
                )
            }
        }
    }
}

@Composable
fun PlantDetailsContent(
    plant: Plant,
    onPlantImageClicked: (plantId: Int) -> Unit,
    onPlantChosen: (() -> Unit)?,
    isPlantButtonVisible: Boolean,
    onWebSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        PlantImageSection(
            plant = plant,
            onImageClick = { onPlantImageClicked(plant.id) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        PlantNameSection(plant = plant)

        Spacer(modifier = Modifier.height(24.dp))

        PlantDetailsGrid(plant = plant, modifier = Modifier.weight(1f))

        Spacer(modifier = Modifier.height(16.dp))

        if (!plant.cultivationDetails.isNullOrEmpty()) {
            CultivationDetailsSection(details = plant.cultivationDetails)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            WebSearchButton(
                onClick = onWebSearch
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isPlantButtonVisible) {
            RoundedButton(
                onClick = {
                    onPlantChosen?.invoke()
                },
                modifier = Modifier.fillMaxWidth(),
                text = "Plant!"
            )
        }
    }
}
