package szysz3.planty.screen.plantid.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import szysz3.planty.R
import szysz3.planty.screen.plantid.composable.PlantResultCard
import szysz3.planty.screen.plantid.viewmodel.PlantIdViewModel
import szysz3.planty.util.PermissionUtils

@Composable
fun PlantIdScreen(viewModel: PlantIdViewModel = hiltViewModel()) {
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

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.plant_id_screen_bcg), // Replace with your background image
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Adjust as needed (Crop, Fit, FillBounds, etc.)
        )

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            uiState.identifiedPlants?.let { plants ->
                // List of identification results
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(plants) { plant ->
                        PlantResultCard(plantName = plant.name, confidence = plant.confidence)
                    }
                }
            } ?: uiState.errorMessage?.let { error ->
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                viewModel.clearErrorMessage()
            }
        }

        if (!uiState.isLoading) {
            FloatingActionButton(
                onClick = {
                    if (PermissionUtils.hasCameraPermission(context)) {
                        viewModel.createPhotoFile()
                        shouldLaunchCamera = true
                    } else {
                        permissionLauncher.launch(android.Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Rounded.Search, contentDescription = "Identify plant")
            }
        }
    }
}