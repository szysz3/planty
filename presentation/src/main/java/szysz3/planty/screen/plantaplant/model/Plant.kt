package szysz3.planty.screen.plantaplant.model

data class Plant(
    val id: Int,
    val latinName: String,
    val commonName: String?,
    val height: Float?,
    val width: Float?,
    val soil: String?,
    val ph: String?,
    val growthRate: String?,
    val deciduousEvergreen: String?,
    val shade: String?,
    val imageRes: Int
)