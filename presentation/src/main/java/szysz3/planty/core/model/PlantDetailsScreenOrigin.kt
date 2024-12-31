package szysz3.planty.core.model

enum class PlantDetailsScreenOrigin(val value: Int) {
    MY_GARDEN(0),
    PLANT_CATALOG_MY_GARDEN(1),
    PLANT_CATALOG_BOTTOM_BAR(2),
    PLANT_ID_SCREEN(3);

    companion object {
        fun fromValue(value: Int): PlantDetailsScreenOrigin {
            return when (value) {
                0 -> MY_GARDEN
                1 -> PLANT_CATALOG_MY_GARDEN
                2 -> PLANT_CATALOG_BOTTOM_BAR
                3 -> PLANT_ID_SCREEN
                else -> throw IllegalArgumentException("Invalid value: $value")
            }
        }

        fun fromPlantCatalogScreenOrigin(value: PlantCatalogScreenOrigin): PlantDetailsScreenOrigin {
            return when (value) {
                PlantCatalogScreenOrigin.MY_GARDEN -> PLANT_CATALOG_MY_GARDEN
                PlantCatalogScreenOrigin.BOTTOM_BAR -> PLANT_CATALOG_BOTTOM_BAR
            }
        }
    }
}