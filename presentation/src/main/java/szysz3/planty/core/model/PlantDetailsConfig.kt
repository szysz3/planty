package szysz3.planty.core.model

enum class PlantDetailsConfig(val value: Int) {
    PLANT(0),
    DELETE(1),
    PREVIEW(2);

    companion object {
        fun fromValue(value: Int): PlantDetailsConfig {
            return when (value) {
                0 -> PLANT
                1 -> DELETE
                2 -> PREVIEW
                else -> throw IllegalArgumentException("Invalid value: $value")
            }
        }
    }
}