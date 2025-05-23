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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import szysz3.planty.screen.base.topbar.TopBarActionButton
import szysz3.planty.screen.base.topbar.TopBarBackButton
import szysz3.planty.screen.plantdetails.composable.CultivationDetailsSection
import szysz3.planty.screen.plantdetails.composable.PlantDetailsGrid
import szysz3.planty.screen.plantdetails.composable.PlantImageSection
import szysz3.planty.screen.plantdetails.composable.PlantNameSection
import szysz3.planty.screen.plantdetails.composable.WebSearchButton
import szysz3.planty.screen.plantdetails.viewmodel.PlantDetailsViewModel
import szysz3.planty.theme.dimensions

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
            TopBarActionButton(
                showButton = uiState.isDeleteButtonVisible,
                onAction = { plantDetailsViewModel.showDeleteDialog(true) }
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
                    title = stringResource(R.string.delete_plant),
                    message = stringResource(R.string.delete_plant_confirmation),
                    confirmButtonText = stringResource(R.string.delete_button),
                    dismissButtonText = stringResource(R.string.cancel_button),
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
    val dimens = MaterialTheme.dimensions()
    Column(
        modifier = modifier
            .padding(dimens.spacing16)
            .verticalScroll(rememberScrollState())
    ) {
        PlantImageSection(
            plant = plant,
            onImageClick = { onPlantImageClicked(plant.id) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimens.size24))

        PlantNameSection(plant = plant)

        Spacer(modifier = Modifier.height(dimens.size2))

        PlantDetailsGrid(plant = plant, modifier = Modifier.weight(1f))

        Spacer(modifier = Modifier.height(dimens.size16))

        if (!plant.cultivationDetails.isNullOrEmpty()) {
            CultivationDetailsSection(details = plant.cultivationDetails)
        }

        Spacer(modifier = Modifier.height(dimens.size16))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            WebSearchButton(
                onClick = onWebSearch
            )
        }

        Spacer(modifier = Modifier.height(dimens.size16))

        if (isPlantButtonVisible) {
            RoundedButton(
                onClick = {
                    onPlantChosen?.invoke()
                },
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.plant_button)
            )
        }
    }
}