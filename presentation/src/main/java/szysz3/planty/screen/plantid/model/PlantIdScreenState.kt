package szysz3.planty.screen.plantid.model

import android.net.Uri

data class PlantIdScreenState(
    val photoUri: Uri? = null,
    val plantIdResult: PlantIdState = PlantIdState.Idle,
    val photoUploaded: Boolean = false
)