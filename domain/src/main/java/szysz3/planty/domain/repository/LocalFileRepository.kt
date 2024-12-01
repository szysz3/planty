package szysz3.planty.domain.repository

import android.net.Uri

interface LocalFileRepository {
    suspend fun createFile(): Uri?
    suspend fun deleteFile(uri: Uri): Boolean
}