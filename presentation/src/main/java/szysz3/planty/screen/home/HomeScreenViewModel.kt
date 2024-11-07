package szysz3.planty.screen.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import szysz3.planty.domain.model.SampleData
import szysz3.planty.domain.usecase.SampleUseCase
import szysz3.planty.domain.usecase.base.NoParams
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val sampleUseCase: SampleUseCase) : ViewModel() {

    private val _sampleData = MutableStateFlow<SampleData?>(null)
    val sampleData: StateFlow<SampleData?> get() = _sampleData

    fun getSampleData() {
        _sampleData.value = sampleUseCase.execute(NoParams())
    }
}