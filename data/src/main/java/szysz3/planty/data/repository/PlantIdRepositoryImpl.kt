package szysz3.planty.data.repository

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import szysz3.planty.domain.repository.PlantIdRepository
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

class PlantIdRepositoryImpl @Inject constructor() : PlantIdRepository {
    private val storage = Firebase.storage

    override suspend fun uploadPlantImage(imageData: ByteArray): Uri? {
        val storageRef = storage.reference
        val imageRef = storageRef.child("plant_images/${UUID.randomUUID()}.jpg")

        return try {
            imageRef.putBytes(imageData).await()
            imageRef.downloadUrl.await()
        } catch (e: Exception) {
            Timber.e(e, "Error uploading plant image: ${e.message}")
            null // Return null in case of an error
        }
    }
}