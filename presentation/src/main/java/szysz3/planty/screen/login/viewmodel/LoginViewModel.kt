package szysz3.planty.screen.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import szysz3.planty.domain.model.AuthResult
import szysz3.planty.domain.model.AuthState
import szysz3.planty.domain.usecase.GetAuthStateUseCase
import szysz3.planty.domain.usecase.LoginWithGoogleUseCase
import szysz3.planty.domain.usecase.LogoutUseCase
import szysz3.planty.domain.usecase.base.NoParams
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getAuthStateUseCase: GetAuthStateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val uiState: StateFlow<AuthResult> = _uiState

    private val _authState = MutableStateFlow(AuthState.LOGGED_OUT)
    val authState: StateFlow<AuthState> = _authState

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            getAuthStateUseCase(NoParams()).collect { state ->
                _authState.value = state
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = AuthResult.Loading
            _uiState.value = loginWithGoogleUseCase(idToken)
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase(NoParams())
            _uiState.value = AuthResult.Success(null)
        }
    }
}
