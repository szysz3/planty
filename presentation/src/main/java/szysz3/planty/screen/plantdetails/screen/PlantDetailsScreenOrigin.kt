package szysz3.planty.screen.plantdetails.screen

enum class PlantDetailsScreenOrigin(val value: Int) {
    HOME_SCREEN(0),
    PLANT_A_PLANT_SCREEN(1);

    companion object {
        fun fromValue(value: Int): PlantDetailsScreenOrigin {
            return when (value) {
                0 -> HOME_SCREEN
                1 -> PLANT_A_PLANT_SCREEN
                else -> throw IllegalArgumentException("Invalid value: $value")
            }
        }
    }
}