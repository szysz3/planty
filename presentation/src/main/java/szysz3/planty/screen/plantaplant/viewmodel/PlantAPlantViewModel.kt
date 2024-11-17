package szysz3.planty.screen.plantaplant.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import szysz3.planty.domain.usecase.PlantSearchUseCase
import javax.inject.Inject

@HiltViewModel
class PlantAPlantViewModel @Inject constructor(
    private val plantSearchUseCase: PlantSearchUseCase,
) : ViewModel() {
}