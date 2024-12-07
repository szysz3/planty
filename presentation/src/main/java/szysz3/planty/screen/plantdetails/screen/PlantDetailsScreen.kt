package szysz3.planty.screen.plantdetails.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import szysz3.planty.R
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.mygarden.viewmodel.MyGardenViewModel
import szysz3.planty.screen.plantaplant.viewmodel.PlantAPlantViewModel
import szysz3.planty.screen.plantdetails.model.PlantDetailsScreenOrigin
import szysz3.planty.ui.widgets.DeleteAlertDialog
import szysz3.planty.ui.widgets.EllipticalBackground
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
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = plant.imageRes),
                    contentDescription = plant.latinName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = plant.latinName, style = MaterialTheme.typography.titleLarge)

                plant.commonName?.let { commonName ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = commonName, style = MaterialTheme.typography.bodyLarge)
                }

                Spacer(modifier = Modifier.weight(1f))
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