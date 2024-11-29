package szysz3.planty.domain.repository

import szysz3.planty.domain.model.AuthResult

interface AuthRepository {
    suspend fun loginWithGoogle(idToken: String): AuthResult
    suspend fun getCurrentUserId(): String?
    suspend fun logout()
}