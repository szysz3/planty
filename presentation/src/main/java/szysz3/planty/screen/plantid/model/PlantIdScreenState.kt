package szysz3.planty.screen.plantid.model

import android.net.Uri

/**
 * Represents the UI state for the plant identification screen.
 * Manages the photo selection, identification process state, and upload status.
 *
 * @property photoUri URI of the selected plant photo, null if no photo is selected
 * @property plantIdResult Current state of the plant identification process
 * @property photoUploaded Indicates whether the photo has been successfully uploaded
 */
data class PlantIdScreenState(
    val photoUri: Uri? = null,
    val plantIdResult: PlantIdState = PlantIdState.Idle,
    val photoUploaded: Boolean = false
)