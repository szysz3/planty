package szysz3.planty.screen.plantid.model

import szysz3.planty.core.model.toPresentationModel
import szysz3.planty.domain.model.remote.PlantIdResponse
import szysz3.planty.domain.model.remote.Result

fun PlantIdResponse.toPresentationModel(): List<PlantResult> {
    return results.map { result -> result.toPresentationModel() }
}

fun Result.toPresentationModel(): PlantResult {
    val commonName = species.commonNames?.joinToString(", ")
    val scientificName = species.scientificNameWithoutAuthor

    return PlantResult(
        name = commonName,
        scientificName = scientificName,
        confidence = score,
        plant = plant?.toPresentationModel(),
    )
}