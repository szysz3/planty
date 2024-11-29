package szysz3.planty.screen.plantid.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import szysz3.planty.screen.plantid.model.PlantIdUiState
import javax.inject.Inject

@HiltViewModel
// TODO: use cases
class PlantIdViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {

    private val _uiState = MutableStateFlow(PlantIdUiState())
    val uiState: StateFlow<PlantIdUiState> = _uiState

}
