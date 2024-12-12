package szysz3.planty.screen.plantdetails.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import szysz3.planty.R
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.mygarden.viewmodel.MyGardenViewModel
import szysz3.planty.screen.plantdetails.composable.EvenGrid
import szysz3.planty.screen.plantdetails.model.PlantDetailItem
import szysz3.planty.screen.plantdetails.model.PlantDetailsScreenOrigin
import szysz3.planty.screen.plantdetails.model.mapDeciduousEvergreenToString
import szysz3.planty.screen.plantdetails.model.mapDroughtToString
import szysz3.planty.screen.plantdetails.model.mapFrostTenderToString
import szysz3.planty.screen.plantdetails.model.mapGrowthRateToString
import szysz3.planty.screen.plantdetails.model.mapHardinessToString
import szysz3.planty.screen.plantdetails.model.mapMoistureToString
import szysz3.planty.screen.plantdetails.model.mapPHToString
import szysz3.planty.screen.plantdetails.model.mapPoorSoilToString
import szysz3.planty.screen.plantdetails.model.mapShadeToString
import szysz3.planty.screen.plantdetails.model.mapSoilToString
import szysz3.planty.screen.plantdetails.model.mapWellDrainedToString
import szysz3.planty.screen.plantdetails.viewmodel.PlantDetailsViewModel
import szysz3.planty.ui.widgets.DeleteAlertDialog
import szysz3.planty.ui.widgets.EllipticalBackground
import szysz3.planty.ui.widgets.ImageWithTextHorizontal
import szysz3.planty.ui.widgets.RoundedButton

@Composable
fun PlantDetailsScreen(
    mainScreenViewModel: MainScreenViewModel,
    myGardenViewModel: MyGardenViewModel,
    origin: PlantDetailsScreenOrigin,
    plantId: Int,
    onNavigateBack: () -> Unit,
    onPlantChosen: () -> Unit,
    plantDetailsViewMode: PlantDetailsViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        plantDetailsViewMode.updatePlantId(plantId)

        mainScreenViewModel.updateShowBackButton(true)
        mainScreenViewModel.updateTopBarVisibility(true)
        mainScreenViewModel.updateShowDeleteButton(origin == PlantDetailsScreenOrigin.HOME_SCREEN)
    }

    val uiState by plantDetailsViewMode.uiState.collectAsState()
    val myGardenUiState by myGardenViewModel.uiState.collectAsState()

    EllipticalBackground(R.drawable.bcg3, 0.5f)

    uiState.selectedPlant?.let { plant ->
        val elements = listOfNotNull(
            plant.soil?.takeIf { it.isNotEmpty() }?.let {
                PlantDetailItem(R.drawable.icon_soil, mapSoilToString(it))
            },
            plant.deciduousEvergreen?.takeIf { it.isNotEmpty() }?.let {
                PlantDetailItem(R.drawable.icon_evergreen, mapDeciduousEvergreenToString(it))
            },
            plant.wellDrained?.let {
                PlantDetailItem(R.drawable.icon_drain, mapWellDrainedToString(it))
            },
            plant.frostTender?.let {
                PlantDetailItem(R.drawable.icon_frost, mapFrostTenderToString(it))
            },
            plant.moisture?.let {
                PlantDetailItem(R.drawable.icon_moisture, mapMoistureToString(it))
            },
            plant.shade?.takeIf { it.isNotEmpty() }?.let {
                PlantDetailItem(R.drawable.icon_shade, mapShadeToString(it))
            },
            plant.ph?.takeIf { it.isNotEmpty() }?.let {
                PlantDetailItem(R.drawable.icon_ph, mapPHToString(it))
            },
            plant.poorSoil?.let {
                PlantDetailItem(R.drawable.icon_poor_soil, mapPoorSoilToString(it))
            },
            plant.drought?.let {
                PlantDetailItem(R.drawable.icon_drought, mapDroughtToString(it))
            },
            plant.hardiness?.let {
                PlantDetailItem(R.drawable.icon_temp, mapHardinessToString(it))
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(200.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxSize(),
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.plant_placeholder),
                                contentDescription = plant.latinName,
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        plant.width?.let { width ->
                            ImageWithTextHorizontal(
                                imageRes = R.drawable.icon_width,
                                title = width.toString()
                            )
                        }

                        plant.height?.let { height ->
                            ImageWithTextHorizontal(
                                imageRes = R.drawable.icon_height,
                                title = height.toString()
                            )
                        }
                        if (!plant.growthRate.isNullOrEmpty()) {
                            ImageWithTextHorizontal(
                                imageRes = R.drawable.icon_growth_rate,
                                title = mapGrowthRateToString(plant.growthRate)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = plant.latinName, style = MaterialTheme.typography.headlineMedium)

                plant.commonName?.let { commonName ->
                    Text(
                        text = "($commonName)",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                EvenGrid(
                    items = elements,
                    columns = 2, // Set the number of columns
                    modifier = Modifier.fillMaxWidth(),
                    createItem = { item ->
                        ImageWithTextHorizontal(
                            imageRes = item.imageRes,
                            title = item.title,
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        )
                    }
                )

                if (!plant.cultivationDetails.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    ImageWithTextHorizontal(
                        imageRes = R.drawable.icon_info,
                        title = "Cultivation details",
                        iconSize = 32,
                        textStyle = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = plant.cultivationDetails, modifier = Modifier.padding(8.dp))
                }
            }

            if (origin == PlantDetailsScreenOrigin.PLANT_A_PLANT_SCREEN) {
                RoundedButton(
                    onClick = {
                        myGardenViewModel.saveCell(plant)
                        onPlantChosen()
                    },
                    text = "Plant!"
                )
            }
        }
    }

    if (myGardenUiState.isDeleteDialogVisible) {
        DeleteAlertDialog(
            title = "Delete Plant",
            message = "Are you sure you want to delete this plant?",
            confirmButtonText = "Delete",
            dismissButtonText = "Cancel",
            onConfirmDelete = {
                myGardenViewModel.saveCell(null)
                myGardenViewModel.showDeleteDialog(false)
                onNavigateBack()
            },
            onCancel = {
                myGardenViewModel.showDeleteDialog(false)
            }
        )
    }
}