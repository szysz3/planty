import szysz3.planty.R
import szysz3.planty.screen.home.model.GardenCell
import szysz3.planty.screen.home.model.GardenState
import szysz3.planty.screen.plantaplant.model.Plant
import szysz3.planty.domain.model.GardenCell as GardenCellDomain
import szysz3.planty.domain.model.GardenState as GardenStateDomain
import szysz3.planty.domain.model.Plant as PlantDomain

fun GardenStateDomain.toPresentationModel(): GardenState {
    return GardenState(
        rows = this.rows,
        columns = this.columns,
        cells = this.cells.map { cell ->
            GardenCell(row = cell.row, column = cell.column, plant = cell.plant)
        }
    )
}

fun GardenState.toDomainModel(): GardenStateDomain {
    return GardenStateDomain(
        rows = this.rows,
        columns = this.columns,
        cells = this.cells.map { cell ->
            GardenCellDomain(
                row = cell.row,
                column = cell.column,
                plant = cell.plant
            )
        }
    )
}

fun List<PlantDomain>.toPresentationModel(): List<Plant> {
    return map { plant ->
        Plant(
            latinName = plant.latinName,
            commonName = plant.commonName,
            height = plant.height,
            width = plant.width,
            soil = plant.soil,
            ph = plant.ph,
            growthRate = plant.growthRate,
            deciduousEvergreen = plant.deciduousEvergreen,
            shade = plant.shade,
            // TODO: get plant image from outside
            imageRes = R.drawable.plant_placeholder
        )
    }
}