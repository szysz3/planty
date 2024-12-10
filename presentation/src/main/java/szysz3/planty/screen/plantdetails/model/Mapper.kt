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
        "FS" -> stringResource(R.string.shade_full_sun)
        "FSN" -> stringResource(R.string.shade_partial_sun)
        "N" -> stringResource(R.string.shade_full)
        else -> stringResource(R.string.moisture_unknown)
    }
}

@Composable
fun mapFrostTenderToString(isFrostTender: Boolean): String {
    return if (isFrostTender) {
        stringResource(R.string.frost_tender_yes)
    } else {
        stringResource(R.string.frost_tender_no)
    }
}

@Composable
fun mapHardinessToString(hardiness: Int): String {
    return when (hardiness) {
        3 -> stringResource(R.string.hardiness_zone_3) // -40°C to -34°C
        4 -> stringResource(R.string.hardiness_zone_4) // -34°C to -29°C
        5 -> stringResource(R.string.hardiness_zone_5) // -29°C to -23°C
        6 -> stringResource(R.string.hardiness_zone_6) // -23°C to -18°C
        7 -> stringResource(R.string.hardiness_zone_7) // -18°C to -12°C
        8 -> stringResource(R.string.hardiness_zone_8) // -12°C to -7°C
        10 -> stringResource(R.string.hardiness_zone_10) // -1°C to +4°C
        else -> stringResource(R.string.hardiness_unknown)
    }
}

@Composable
fun mapGrowthRateToString(growthRate: String): String {
    return when (growthRate) {
        "S" -> stringResource(R.string.growth_rate_slow)   // Slow Growth Rate
        "M" -> stringResource(R.string.growth_rate_medium) // Medium Growth Rate
        "F" -> stringResource(R.string.growth_rate_fast)  // Fast Growth Rate
        else -> stringResource(R.string.growth_rate_unknown) // Unknown Growth Rate
    }
}

@Composable
fun mapDeciduousEvergreenToString(deciduousEvergreen: String): String {
    return when (deciduousEvergreen) {
        "D" -> stringResource(R.string.deciduous)       // Deciduous
        "E" -> stringResource(R.string.evergreen)      // Evergreen
        else -> stringResource(R.string.deciduous_evergreen_unknown) // Unknown Type
    }
}

@Composable
fun mapWellDrainedToString(isWellDrained: Boolean?): String {
    return when (isWellDrained) {
        true -> stringResource(R.string.well_drained_yes)    // Well-drained
        false -> stringResource(R.string.well_drained_no)   // Poorly-drained
        null -> stringResource(R.string.well_drained_unknown) // Unknown
    }
}

@Composable
fun mapPoorSoilToString(toleratesPoorSoil: Boolean?): String {
    return when (toleratesPoorSoil) {
        true -> stringResource(R.string.poor_soil_yes)    // Tolerates
        false -> stringResource(R.string.poor_soil_no)   // Needs rich
        null -> stringResource(R.string.poor_soil_unknown) // Unknown
    }
}

@Composable
fun mapDroughtToString(isDroughtTolerant: Boolean?): String {
    return when (isDroughtTolerant) {
        true -> stringResource(R.string.drought_yes)    // Tolerant
        false -> stringResource(R.string.drought_no)   // Sensitive
        null -> stringResource(R.string.drought_unknown) // Unknown
    }
}