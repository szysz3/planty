package szysz3.planty.domain.usecase

import android.net.Uri
import szysz3.planty.domain.repository.FileRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class DeleteTempPhotoFileUseCase @Inject constructor(
    private val fileRepository: FileRepository
) : BaseUseCase<Uri, Boolean>() {
    override suspend fun invoke(input: Uri): Boolean {
        return fileRepository.deleteFile(input)
    }
}