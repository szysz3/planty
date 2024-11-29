package szysz3.planty.domain.usecase

import szysz3.planty.domain.model.AuthResult
import szysz3.planty.domain.repository.AuthRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) : BaseUseCase<String, AuthResult>() {
    override suspend fun invoke(input: String): AuthResult {
        return repository.loginWithGoogle(input)
    }
}