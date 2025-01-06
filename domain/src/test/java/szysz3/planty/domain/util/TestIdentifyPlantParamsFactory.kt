package szysz3.planty.domain.util

import android.net.Uri
import szysz3.planty.domain.usecase.plant.IdentifyPlantsParams

object TestIdentifyPlantsParamsFactory {

    fun createSampleParams(apiKey: String, uri: Uri): IdentifyPlantsParams {
        return IdentifyPlantsParams(
            apiKey = apiKey,
            imageUris = listOf(uri)
        )
    }

    fun createSampleParamsMultipleUris(apiKey: String, uris: List<Uri>): IdentifyPlantsParams {
        return IdentifyPlantsParams(
            apiKey = apiKey,
            imageUris = uris
        )
    }
}