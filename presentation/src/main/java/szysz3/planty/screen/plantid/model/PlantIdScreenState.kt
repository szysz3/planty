package szysz3.planty.screen.plantid.model

import android.net.Uri

data class PlantIdScreenState(
    val photoUri: Uri? = null,
    val isLoading: Boolean = false,
    val photoUploaded: Boolean = false,
    val identifiedPlant: String? = null,
    val errorMessage: String? = null
)