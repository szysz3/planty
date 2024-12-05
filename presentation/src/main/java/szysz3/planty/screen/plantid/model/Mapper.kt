package szysz3.planty.screen.plantid.model

import szysz3.planty.domain.model.remote.PlantIdResponse
import szysz3.planty.domain.model.remote.Result

fun PlantIdResponse.toPresentationModel(): List<PlantResult> {
    return results.map { result -> result.toPresentationModel() }
}

fun Result.toPresentationModel(): PlantResult {
    val commonName = species.commonNames?.joinToString(", ") ?: "Unknown"

    return PlantResult(
        name = commonName,
        confidence = score,
    )
}