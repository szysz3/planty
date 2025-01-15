package szysz3.planty.screen.mygarden.model

import szysz3.planty.core.model.Plant

sealed class MyGardenScreenUiEvent {
    data class OnPlantChosen(val plant: Plant, val row: Int, val column: Int, val gardenId: Int) :
        MyGardenScreenUiEvent()

    data class OnEmptyCellChosen(val row: Int, val column: Int, val gardenId: Int) :
        MyGardenScreenUiEvent()
}