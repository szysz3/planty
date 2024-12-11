package szysz3.planty.screen.plantid.model

import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.model.remote.PlantIdResponse
import szysz3.planty.domain.model.remote.Result
import szysz3.planty.screen.mygarden.model.toPresentationModel

fun PlantIdResponse.toPresentationModel(): List<PlantResult> {
    return results.map { result -> result.toPresentationModel(this.plant) }
}

fun Result.toPresentationModel(plant: Plant?): PlantResult {
    val commonName = species.commonNames?.joinToString(", ")
    val scientificName = species.scientificNameWithoutAuthor

    return PlantResult(
        name = commonName,
        scientificName = scientificName,
        confidence = score,
        plant = plant?.toPresentationModel(),
    )
}