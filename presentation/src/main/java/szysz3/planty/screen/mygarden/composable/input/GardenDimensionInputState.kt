package szysz3.planty.screen.mygarden.composable.input

/**
 * Data class representing the state of garden dimension inputs.
 *
 * @property name The name of the garden (max 10 characters)
 * @property width The string representation of garden width
 * @property height The string representation of garden height
 * @property nameError Whether the name input has an error
 * @property widthError Whether the width input has an error
 * @property heightError Whether the height input has an error
 */
data class GardenDimensionInputState(
    val name: String = "",
    val width: String = "",
    val height: String = "",
    val nameError: Boolean = false,
    val widthError: Boolean = false,
    val heightError: Boolean = false
)