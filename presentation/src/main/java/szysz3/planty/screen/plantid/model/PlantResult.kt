package szysz3.planty.screen.plantid.model

import szysz3.planty.core.model.Plant

/**
 * Represents the result of a plant identification operation.
 * Contains both the identification details and the associated plant data if available.
 *
 * @property name Common name of the identified plant, null if unknown
 * @property scientificName Scientific (Latin) name of the plant, null if unknown
 * @property confidence Confidence score of the identification result (0.0 to 1.0)
 * @property plant Associated plant data object, null if detailed data is not available
 */
data class PlantResult(
    val name: String?,
    val scientificName: String?,
    val confidence: Double,
    val plant: Plant?
)