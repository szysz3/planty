package szysz3.planty.domain.usecase

import android.net.Uri
import szysz3.planty.domain.repository.FileRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import szysz3.planty.domain.usecase.base.NoParams
import javax.inject.Inject

class CreateTempPhotoFileUseCase @Inject constructor(
    private val fileRepository: FileRepository
) : BaseUseCase<NoParams, Uri?>() {
    override suspend fun invoke(input: NoParams): Uri? {
        return fileRepository.createFile()
    }
}