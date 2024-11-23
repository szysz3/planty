package szysz3.planty.screen.plantdetails.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import szysz3.planty.screen.home.viewmodel.HomeScreenViewModel
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.plantaplant.viewmodel.PlantAPlantViewModel
import szysz3.planty.screen.plantdetails.composable.DeletePlantDialog

@Composable
fun PlantDetailsScreen(
    mainScreenViewModel: MainScreenViewModel,
    plantAPlantViewModel: PlantAPlantViewModel,
    homeScreenViewModel: HomeScreenViewModel,
    origin: PlantDetailsScreenOrigin,
    onNavigateBack: () -> Unit,
    onPlantChosen: () -> Unit
) {
    LaunchedEffect(Unit) {
        mainScreenViewModel.showBackButton(true)
        mainScreenViewModel.showDeleteButton(origin == PlantDetailsScreenOrigin.HOME_SCREEN)
    }

    val isDeleteDialogVisible by homeScreenViewModel.isDeleteDialogVisible.collectAsState()
    val plantToPlant by plantAPlantViewModel.selectedPlant.collectAsState()

    val selectedPlant = if (origin == PlantDetailsScreenOrigin.HOME_SCREEN) {
        homeScreenViewModel.getPlantForSelectedCell()
    } else {
        plantToPlant
    }

    selectedPlant?.let { plant ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
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
                Text(
                    text = commonName,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            if (origin == PlantDetailsScreenOrigin.PLANT_A_PLANT_SCREEN) {
                Button(
                    onClick = {
                        homeScreenViewModel.saveCell(plant)
                        onPlantChosen()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Choose Plant")
                }
            }
        }
    }

    if (isDeleteDialogVisible) {
        DeletePlantDialog(
            onConfirmDelete = {
                homeScreenViewModel.saveCell(null)
                homeScreenViewModel.showDeleteDialog(false)
                onNavigateBack()
            },
            onCancel = {
                homeScreenViewModel.showDeleteDialog(false)
            }
        )
    }
}