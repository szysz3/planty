package szysz3.planty.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import szysz3.planty.domain.model.AuthState
import szysz3.planty.domain.repository.AuthRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import szysz3.planty.domain.usecase.base.NoParams
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(
    private val repository: AuthRepository
) : BaseUseCase<NoParams, Flow<AuthState>>() {
    override suspend fun invoke(input: NoParams) = flow {
        val user = repository.getCurrentUserId()
        if (user != null) {
            emit(AuthState.LOGGED_IN)
        } else {
            emit(AuthState.LOGGED_OUT)
        }
    }
}