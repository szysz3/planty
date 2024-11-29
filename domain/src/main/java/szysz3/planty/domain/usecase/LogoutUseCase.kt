package szysz3.planty.domain.usecase

import szysz3.planty.domain.repository.AuthRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import szysz3.planty.domain.usecase.base.NoParams
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) : BaseUseCase<NoParams, Unit>() {
    override suspend fun invoke(input: NoParams) {
        repository.logout()
    }
}