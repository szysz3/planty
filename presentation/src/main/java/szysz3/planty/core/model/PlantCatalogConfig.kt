package szysz3.planty.core.model

enum class PlantCatalogConfig(val value: Int) {
    PLANT(0),
    PREVIEW(1);

    companion object {
        fun fromValue(value: Int): PlantCatalogConfig {
            return when (value) {
                0 -> PLANT
                1 -> PREVIEW
                else -> throw IllegalArgumentException("Invalid value: $value")
            }
        }
    }
}