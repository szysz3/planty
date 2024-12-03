package szysz3.planty.screen.plantid.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Button
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Plant Identification", style = MaterialTheme.typography.h4)

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            uiState.identifiedPlant?.let { plant ->
                Text(text = "Identified Plant: $plant", style = MaterialTheme.typography.body1)
            } ?: uiState.errorMessage?.let { error ->
                Text(text = error, color = MaterialTheme.colors.error)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (PermissionUtils.hasCameraPermission(context)) {
                viewModel.createPhotoFile()
                shouldLaunchCamera = true
            } else {
                permissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }) {
            Text("Take Photo")
        }
    }
}