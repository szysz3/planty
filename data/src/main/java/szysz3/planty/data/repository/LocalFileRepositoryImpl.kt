package szysz3.planty.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import szysz3.planty.domain.repository.LocalFileRepository
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [LocalFileRepository] that handles local file operations.
 * Provides functionality for creating temporary files and managing their lifecycle.
 *
 * @property context Application context used for file operations
 */
@Singleton
class LocalFileRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocalFileRepository {

    /**
     * Creates a temporary file for image storage.
     *
     * @return URI of the created file, or null if creation fails
     */
    override suspend fun createFile(): Uri? {
        return try {
            withContext(Dispatchers.IO) {
                val fileName = UUID.randomUUID().toString()
                val file = File.createTempFile(
                    fileName,
                    FILE_EXTENSION,
                    context.cacheDir
                )

                FileProvider.getUriForFile(
                    context,
                    context.packageName + FILE_PROVIDER_AUTHORITY_SUFFIX,
                    file
                )
            }
        } catch (e: IOException) {
            Timber.e(e, "Failed to create file: $e")
            null
        }
    }

    /**
     * Deletes a file specified by the given URI.
     *
     * @param uri URI of the file to delete
     * @return true if deletion was successful, false otherwise
     */
    override suspend fun deleteFile(uri: Uri): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val rowsDeleted = context.contentResolver.delete(uri, null, null)
                rowsDeleted > 0
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to delete file: $e")
            false
        }
    }

    companion object {
        private const val FILE_EXTENSION = ".jpg"
        private const val FILE_PROVIDER_AUTHORITY_SUFFIX = ".fileprovider"
    }
}