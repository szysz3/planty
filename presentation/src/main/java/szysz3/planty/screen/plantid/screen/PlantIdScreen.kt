package szysz3.planty.screen.plantid.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import szysz3.planty.R
import szysz3.planty.screen.base.BaseScreen
import szysz3.planty.screen.plantid.composable.PlantResultCard
import szysz3.planty.screen.plantid.viewmodel.PlantIdViewModel
import szysz3.planty.ui.widgets.EllipticalBackground
import szysz3.planty.ui.widgets.FloatingActionButton
import szysz3.planty.util.PermissionUtils
import szysz3.planty.util.openWebSearch

@Composable
fun PlantIdScreen(
    title: String,
    navController: NavHostController,
    viewModel: PlantIdViewModel = hiltViewModel(),
    onShowPlantDetails: (plantId: Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var shouldLaunchCamera by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.createPhotoFile()
            shouldLaunchCamera = true
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.identifyPlant()
        }
    }

    LaunchedEffect(uiState.photoUri, shouldLaunchCamera) {
        uiState.photoUri?.let {
            if (shouldLaunchCamera) {
                shouldLaunchCamera = false
                takePictureLauncher.launch(it)
            }
        }
    }

    BaseScreen(
        title = title,
        showTopBar = true,
        showBottomBar = true,
        navController = navController
    ) { padding ->

        EllipticalBackground(R.drawable.bcg4)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
            } else {
                uiState.identifiedPlants?.let { plants ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(plants) { plantIdResult ->
                            PlantResultCard(plantResult = plantIdResult) { plant ->
                                if (plant != null) {
                                    onShowPlantDetails(plant.id)
                                } else {
                                    openWebSearch(plantIdResult.scientificName, context)
                                }
                            }
                        }
                    }
                } ?: uiState.errorMessage?.let { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    viewModel.clearErrorMessage()
                }
            }

            AnimatedVisibility(
                visible = uiState.identifiedPlants?.isEmpty() == true || !uiState.isLoading,
            ) {
                FloatingActionButton(
                    icon = Icons.Rounded.Search,
                    contentDescription = "Identify plant",
                    onClick = {
                        if (PermissionUtils.hasCameraPermission(context)) {
                            viewModel.createPhotoFile()
                            shouldLaunchCamera = true
                        } else {
                            permissionLauncher.launch(android.Manifest.permission.CAMERA)
                        }
                    })
            }

            AnimatedVisibility(
                visible = uiState.identifiedPlants?.isNotEmpty() == true,
            ) {
                FloatingActionButton(
                    icon = Icons.Rounded.Clear, // Replace with your desired icon
                    contentDescription = "Second action",
                    onClick = {
                        viewModel.clearResults()
                    }
                )
            }

        }

    }
}