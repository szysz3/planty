package szysz3.planty.screen.plantaplant.model

import szysz3.planty.R
import java.util.UUID.randomUUID

data class Plant(
    val id: String,
    val name: String,
    val shortDescription: String,
    val detailedDescription: String,
    val imageRes: Int
) {
    companion object {
        fun random(): Plant {
            return Plant(
                id = randomUUID().toString(),
                name = randomUUID().toString(),
                shortDescription = randomUUID().toString(),
                detailedDescription = randomUUID().toString(),
                imageRes = R.drawable.plant_placeholder
            )
        }

    }
}