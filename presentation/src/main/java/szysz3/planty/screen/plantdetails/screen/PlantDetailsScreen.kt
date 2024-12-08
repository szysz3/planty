package szysz3.planty.screen.plantdetails.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import szysz3.planty.R
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.mygarden.viewmodel.MyGardenViewModel
import szysz3.planty.screen.plantaplant.viewmodel.PlantAPlantViewModel
import szysz3.planty.screen.plantdetails.model.PlantDetailsScreenOrigin
import szysz3.planty.ui.widgets.DeleteAlertDialog
import szysz3.planty.ui.widgets.EllipticalBackground
import szysz3.planty.ui.widgets.ImageWithTextHorizontal
import szysz3.planty.ui.widgets.RoundedButton

@Composable
fun PlantDetailsScreen(
    mainScreenViewModel: MainScreenViewModel,
    plantAPlantViewModel: PlantAPlantViewModel,
    myGardenViewModel: MyGardenViewModel,
    origin: PlantDetailsScreenOrigin,
    onNavigateBack: () -> Unit,
    onPlantChosen: () -> Unit
) {
    LaunchedEffect(Unit) {
        mainScreenViewModel.updateShowBackButton(true)
        mainScreenViewModel.updateShowDeleteButton(origin == PlantDetailsScreenOrigin.HOME_SCREEN)
    }

    val uiState by myGardenViewModel.uiState.collectAsState()
    val plantUiState by plantAPlantViewModel.uiState.collectAsState()

    val selectedPlant = when (origin) {
        PlantDetailsScreenOrigin.HOME_SCREEN -> myGardenViewModel.getPlantForSelectedCell()
        PlantDetailsScreenOrigin.PLANT_A_PLANT_SCREEN -> plantUiState.selectedPlant
    }

    EllipticalBackground(R.drawable.plant_a_plant_screen_bcg)

    selectedPlant?.let { plant ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
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
                                painter = painterResource(id = plant.imageRes),
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
//                                        title = width.toString()
                                title = "15,5m"
                            )
                        }

                        plant.height?.let { height ->
                            ImageWithTextHorizontal(
                                imageRes = R.drawable.icon_height,
//                                        title = height.toString()
                                title = "15,5m"
                            )
                        }
                        if (!plant.growthRate.isNullOrEmpty()) {
                            ImageWithTextHorizontal(
                                imageRes = R.drawable.icon_growth_rate,
//                                title = plant.growthRate
                                title = "Very fast growing"
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

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        if (!plant.soil.isNullOrEmpty()) {
                            ImageWithTextHorizontal(
                                imageRes = R.drawable.icon_soil,
//                                        title = width.toString()
                                title = "well drained, feritile"
                            )
                        }
                        if (!plant.deciduousEvergreen.isNullOrEmpty()) {
                            ImageWithTextHorizontal(
                                imageRes = R.drawable.icon_evergreen,
//                                        title = width.toString()
                                title = "yes"
                            )
                        }
                        plant.wellDrained?.let { wellDrained ->
                            ImageWithTextHorizontal(
                                imageRes = R.drawable.icon_drain,
//                                        title = width.toString()
                                title = "yes"
                            )
                        }
                        plant.frostTender?.let { frostTender ->
                            ImageWithTextHorizontal(
                                imageRes = R.drawable.icon_frost,
//                                        title = width.toString()
                                title = "yes"
                            )
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        if (!plant.shade.isNullOrEmpty()) {
                            ImageWithTextHorizontal(
                                imageRes = R.drawable.icon_shade,
//                                        title = width.toString()
                                title = "full sun"
                            )
                        }
                        if (!plant.ph.isNullOrEmpty()) {
                            ImageWithTextHorizontal(
                                imageRes = R.drawable.icon_ph,
//                                        title = width.toString()
                                title = "7"
                            )
                        }
                        plant.poorSoil?.let { poorSoil ->
                            ImageWithTextHorizontal(
                                imageRes = R.drawable.icon_poor_soil,
//                                        title = width.toString()
                                title = "yes"
                            )
                        }
                        plant.drought?.let { drought ->
                            ImageWithTextHorizontal(
                                imageRes = R.drawable.icon_drought,
//                                        title = width.toString()
                                title = "yes"
                            )
                        }
                    }
                }

                // TODO: rest of the content goes here


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
                    modifier = Modifier.align(Alignment.BottomCenter),
                    text = "Plant!"
                )
            }
        }
    }

    if (uiState.isDeleteDialogVisible) {
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