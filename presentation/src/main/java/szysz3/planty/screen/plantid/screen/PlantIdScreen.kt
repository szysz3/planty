package szysz3.planty.screen.plantid.screen

import android.Manifest
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
import szysz3.planty.core.composable.EllipticalBackground
import szysz3.planty.core.composable.FloatingActionButton
import szysz3.planty.core.util.PermissionUtils
import szysz3.planty.core.util.openWebSearch
import szysz3.planty.screen.base.BaseScreen
import szysz3.planty.screen.plantid.composable.PlantResultCard
import szysz3.planty.screen.plantid.model.PlantIdScreenState
import szysz3.planty.screen.plantid.model.PlantIdState
import szysz3.planty.screen.plantid.viewmodel.PlantIdViewModel

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
        PlantIdContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            uiState = uiState,
            onIdentifyPlant = {
                if (PermissionUtils.hasCameraPermission(context)) {
                    viewModel.createPhotoFile()
                    shouldLaunchCamera = true
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            onClearResults = viewModel::clearResults,
            onShowPlantDetails = onShowPlantDetails,
            context = context
        )
    }
}

@Composable
private fun PlantIdContent(
    modifier: Modifier = Modifier,
    uiState: PlantIdScreenState,
    onIdentifyPlant: () -> Unit,
    onClearResults: () -> Unit,
    onShowPlantDetails: (plantId: Int) -> Unit,
    context: android.content.Context
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when (val result = uiState.plantIdResult) {
            is PlantIdState.Idle -> { //Empty state
            }

            is PlantIdState.Loading -> {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
            }

            is PlantIdState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(result.plants) { plantIdResult ->
                        PlantResultCard(plantResult = plantIdResult) { plant ->
                            if (plant != null) {
                                onShowPlantDetails(plant.id)
                            } else {
                                openWebSearch(plantIdResult.scientificName, context)
                            }
                        }
                    }
                }
            }

            is PlantIdState.Error -> {
                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                LaunchedEffect(Unit) {
                    onClearResults()
                }
            }
        }

        AnimatedVisibility(
            visible = (uiState.plantIdResult is PlantIdState.Idle
                    || uiState.plantIdResult is PlantIdState.Error)
        ) {
            FloatingActionButton(
                icon = Icons.Rounded.Search,
                contentDescription = "Identify plant",
                onClick = onIdentifyPlant
            )
        }

        AnimatedVisibility(
            visible = uiState.plantIdResult is PlantIdState.Success
        ) {
            FloatingActionButton(
                icon = Icons.Rounded.Clear,
                contentDescription = "Clear results",
                onClick = onClearResults
            )
        }
    }
}