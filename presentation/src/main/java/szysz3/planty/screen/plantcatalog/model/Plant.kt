package szysz3.planty.screen.plantcatalog.model

data class Plant(
    val id: Int,
    val latinName: String,
    val commonName: String? = null,
    val height: Float? = null,
    val width: Float? = null,
    val soil: String? = null,
    val ph: String? = null,
    val growthRate: String? = null,
    val deciduousEvergreen: String? = null,
    val shade: String? = null,
    val moisture: String? = null,
    val wellDrained: Boolean? = null,
    val poorSoil: Boolean? = null,
    val drought: Boolean? = null,
    val frostTender: Boolean? = null,
    val hardiness: Int? = null,
    val cultivationDetails: String? = null,
    val imageUrls: List<String?>? = null
)