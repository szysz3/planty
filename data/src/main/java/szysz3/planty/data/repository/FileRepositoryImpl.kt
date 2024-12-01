package szysz3.planty.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import szysz3.planty.domain.repository.FileRepository
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) :
    FileRepository {
    override suspend fun createFile(): Uri? {
        return try {
            val file = withContext(Dispatchers.IO) {
                File.createTempFile(UUID.randomUUID().toString(), ".jpg", context.cacheDir)
            }

            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: IOException) {
            Timber.e("Error creating file: ${e.message}")
            return null
        }
    }

    override suspend fun deleteFile(uri: Uri): Boolean {
        return try {
            val rowsDeleted = context.contentResolver.delete(uri, null, null)
            rowsDeleted > 0
        } catch (e: Exception) {
            Timber.e("Error deleting file: ${e.message}")
            false
        }
    }
}