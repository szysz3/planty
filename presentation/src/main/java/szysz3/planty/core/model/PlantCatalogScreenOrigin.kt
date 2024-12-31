package szysz3.planty.core.model

enum class PlantCatalogScreenOrigin(val value: Int) {
    MY_GARDEN(0),
    BOTTOM_BAR(1);

    companion object {
        fun fromValue(value: Int): PlantCatalogScreenOrigin {
            return when (value) {
                0 -> MY_GARDEN
                1 -> BOTTOM_BAR
                else -> throw IllegalArgumentException("Invalid value: $value")
            }
        }
    }
}