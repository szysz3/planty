package szysz3.planty.screen.plantdetails.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import szysz3.planty.R

@Composable
fun mapMoistureToString(moisture: String): String {
    return when (moisture) {
        "M" -> stringResource(R.string.moisture_medium)
        "DM" -> stringResource(R.string.moisture_dry_to_medium)
        "MWe" -> stringResource(R.string.moisture_medium_with_wetness)
        "DMWe" -> stringResource(R.string.moisture_dry_to_medium_to_wet)
        "We" -> stringResource(R.string.moisture_wet)
        else -> stringResource(R.string.moisture_unknown)
    }
}

@Composable
fun mapPHToString(ph: String): String {
    return when (ph) {
        "A" -> stringResource(R.string.ph_acidic)
        "N" -> stringResource(R.string.ph_neutral)
        "B" -> stringResource(R.string.ph_alkaline)
        "AN" -> stringResource(R.string.ph_acidic_to_neutral)
        "NB" -> stringResource(R.string.ph_neutral_to_alkaline)
        "ANB" -> stringResource(R.string.ph_all_levels)
        else -> stringResource(R.string.ph_unknown)
    }
}

@Composable
fun mapSoilToString(soil: String): String {
    return when (soil) {
        "L" -> stringResource(R.string.soil_light)
        "LM" -> stringResource(R.string.soil_loamy)
        "LMH" -> stringResource(R.string.soil_loamy_heavy)
        else -> stringResource(R.string.soil_unknown)
    }
}

@Composable
fun mapShadeToString(shade: String): String {
    return when (shade) {
        "SN" -> stringResource(R.string.shade_sun_partial)
        "FS" -> stringResource(R.string.shade_full_shade)
        "FSN" -> stringResource(R.string.shade_full_to_partial)
        "S" -> stringResource(R.string.shade_partial)
        "N" -> stringResource(R.string.shade_full_sun)
        else -> stringResource(R.string.shade_unknown)
    }
}

@Composable
fun mapFrostTenderToString(isFrostTender: Boolean?): String {
    return when (isFrostTender) {
        true -> stringResource(R.string.frost_tender_yes)
        false -> stringResource(R.string.frost_tender_no)
        null -> stringResource(R.string.frost_tender_unknown)
    }
}

@Composable
fun mapHardinessToString(hardiness: Int): String {
    return when (hardiness) {
        2 -> stringResource(R.string.hardiness_zone_2)
        3 -> stringResource(R.string.hardiness_zone_3)
        4 -> stringResource(R.string.hardiness_zone_4)
        5 -> stringResource(R.string.hardiness_zone_5)
        6 -> stringResource(R.string.hardiness_zone_6)
        7 -> stringResource(R.string.hardiness_zone_7)
        8 -> stringResource(R.string.hardiness_zone_8)
        9 -> stringResource(R.string.hardiness_zone_9)
        10 -> stringResource(R.string.hardiness_zone_10)
        else -> stringResource(R.string.hardiness_unknown)
    }
}

@Composable
fun mapGrowthRateToString(growthRate: String): String {
    return when (growthRate) {
        "S" -> stringResource(R.string.growth_rate_slow)
        "M" -> stringResource(R.string.growth_rate_medium)
        "F" -> stringResource(R.string.growth_rate_fast)
        else -> stringResource(R.string.growth_rate_unknown)
    }
}

@Composable
fun mapDeciduousEvergreenToString(deciduousEvergreen: String): String {
    return when (deciduousEvergreen) {
        "D" -> stringResource(R.string.deciduous)
        "E" -> stringResource(R.string.evergreen)
        else -> stringResource(R.string.deciduous_evergreen_unknown)
    }
}

@Composable
fun mapWellDrainedToString(isWellDrained: Boolean?): String {
    return when (isWellDrained) {
        true -> stringResource(R.string.well_drained_yes)
        false -> stringResource(R.string.well_drained_no)
        null -> stringResource(R.string.well_drained_unknown)
    }
}

@Composable
fun mapPoorSoilToString(toleratesPoorSoil: Boolean?): String {
    return when (toleratesPoorSoil) {
        true -> stringResource(R.string.poor_soil_yes)
        false -> stringResource(R.string.poor_soil_no)
        null -> stringResource(R.string.poor_soil_unknown)
    }
}

@Composable
fun mapDroughtToString(isDroughtTolerant: Boolean?): String {
    return when (isDroughtTolerant) {
        true -> stringResource(R.string.drought_yes)
        false -> stringResource(R.string.drought_no)
        null -> stringResource(R.string.drought_unknown)
    }
}
