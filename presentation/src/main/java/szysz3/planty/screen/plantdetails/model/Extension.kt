package szysz3.planty.screen.plantdetails.model

import androidx.compose.runtime.Composable
import szysz3.planty.R
import szysz3.planty.core.model.Plant

@Composable
fun Plant.toDetailItems(): List<PlantDetailItem> {
    return listOfNotNull(
        soil?.takeIf { it.isNotEmpty() }?.let {
            PlantDetailItem(R.drawable.icon_soil, mapSoilToString(it))
        },
        deciduousEvergreen?.takeIf { it.isNotEmpty() }?.let {
            PlantDetailItem(R.drawable.icon_evergreen, mapDeciduousEvergreenToString(it))
        },
        wellDrained?.let {
            PlantDetailItem(R.drawable.icon_drain, mapWellDrainedToString(it))
        },
        frostTender?.let {
            PlantDetailItem(R.drawable.icon_frost, mapFrostTenderToString(it))
        },
        moisture?.let {
            PlantDetailItem(R.drawable.icon_moisture, mapMoistureToString(it))
        },
        shade?.takeIf { it.isNotEmpty() }?.let {
            PlantDetailItem(R.drawable.icon_shade, mapShadeToString(it))
        },
        ph?.takeIf { it.isNotEmpty() }?.let {
            PlantDetailItem(R.drawable.icon_ph, mapPHToString(it))
        },
        poorSoil?.let {
            PlantDetailItem(R.drawable.icon_poor_soil, mapPoorSoilToString(it))
        },
        drought?.let {
            PlantDetailItem(R.drawable.icon_drought, mapDroughtToString(it))
        },
        hardiness?.let {
            PlantDetailItem(R.drawable.icon_temp, mapHardinessToString(it))
        }
    )
}

fun Plant.hasAdditionalInfo(): Boolean {
    return width != null || height != null || !growthRate.isNullOrEmpty()
}