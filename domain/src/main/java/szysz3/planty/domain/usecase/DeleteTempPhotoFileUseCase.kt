package szysz3.planty.domain.usecase

import android.net.Uri
import szysz3.planty.domain.repository.LocalFileRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class DeleteTempPhotoFileUseCase @Inject constructor(
    private val fileRepository: LocalFileRepository
) : BaseUseCase<Uri, Boolean>() {
    override suspend fun invoke(input: Uri): Boolean {
        return fileRepository.deleteFile(input)
    }
}