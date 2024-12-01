package szysz3.planty.domain.repository

import android.net.Uri

interface FileRepository {
    suspend fun createFile(): Uri?
    suspend fun deleteFile(uri: Uri): Boolean
}