package szysz3.planty.screen.mygarden.model

import szysz3.planty.core.model.Plant

/**
 * Represents user interface events that can occur in the [MyGardenScreen].
 * This sealed class hierarchy defines all possible UI interactions related to cell selection
 * and plant placement.
 */
sealed class MyGardenScreenUiEvent {
    data class OnPlantChosen(val plant: Plant, val row: Int, val column: Int, val gardenId: Int) :
        MyGardenScreenUiEvent()

    data class OnEmptyCellChosen(val row: Int, val column: Int, val gardenId: Int) :
        MyGardenScreenUiEvent()
}