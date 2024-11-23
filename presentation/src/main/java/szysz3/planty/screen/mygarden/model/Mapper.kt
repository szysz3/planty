import szysz3.planty.R
import szysz3.planty.screen.mygarden.model.GardenCell
import szysz3.planty.screen.mygarden.model.GardenState
import szysz3.planty.screen.plantaplant.model.Plant
import szysz3.planty.domain.model.GardenCell as GardenCellDomain
import szysz3.planty.domain.model.GardenState as GardenStateDomain
import szysz3.planty.domain.model.Plant as PlantDomain

fun GardenStateDomain.toPresentationModel(): GardenState {
    return GardenState(
        rows = this.rows,
        columns = this.columns,
        cells = this.cells.map { cell ->
            GardenCell(
                id = cell.id,
                row = cell.row,
                column = cell.column,
                plant = cell.plant?.toPresentationModel()
            )
        }
    )
}

fun GardenState.toDomainModel(): GardenStateDomain {
    return GardenStateDomain(
        rows = this.rows,
        columns = this.columns,
        cells = this.cells.map { cell ->
            GardenCellDomain(
                id = cell.id,
                row = cell.row,
                column = cell.column,
                plant = cell.plant?.toDomain()
            )
        }
    )
}

fun Plant.toDomain(): PlantDomain {
    return PlantDomain(
        id = this.id,
        latinName = this.latinName,
        commonName = this.commonName,
        height = this.height,
        width = this.width,
        soil = this.soil,
        ph = this.ph,
        growthRate = this.growthRate,
        deciduousEvergreen = this.deciduousEvergreen,
        shade = this.shade,
    )
}

fun PlantDomain.toPresentationModel(): Plant {
    return Plant(
        id = this.id,
        latinName = this.latinName,
        commonName = this.commonName,
        height = this.height,
        width = this.width,
        soil = this.soil,
        ph = this.ph,
        growthRate = this.growthRate,
        deciduousEvergreen = this.deciduousEvergreen,
        shade = this.shade,
        // TODO: get plant image from outside
        imageRes = R.drawable.plant_placeholder
    )
}

fun List<PlantDomain>.toPresentationModel(): List<Plant> {
    return map { plant ->
        plant.toPresentationModel()
    }
}