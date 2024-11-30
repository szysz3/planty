package szysz3.planty.screen.plantid.screen

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import szysz3.planty.screen.plantid.viewmodel.PlantIdViewModel
import timber.log.Timber

@Composable
fun PlantIdScreen(viewModel: PlantIdViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        imageBitmap = it
        Timber.d("bitmap size: width: ${imageBitmap?.width}, height: ${imageBitmap?.height}")
        it?.let { bitmap ->
            isLoading = true
            viewModel.uploadPhoto(bitmap) { uploadedUri ->
                isLoading = false
                imageUri = uploadedUri
                Timber.d("image uri: $imageUri")
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

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            if (imageUri != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Uploaded Plant Image",
                    modifier = Modifier
                        .fillMaxWidth()
                )
            } else if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap?.asImageBitmap() ?: return@Column,
                    contentDescription = "Preview Plant Image",
                    modifier = Modifier.size(200.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Image Selected",
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { launcher.launch() }) {
            Text("Take a Photo")
        }

        if (imageUri != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Photo uploaded successfully!",
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center
            )
        }
    }
}