import szysz3.planty.screen.home.model.GardenCell
import szysz3.planty.screen.home.model.GardenState
import szysz3.planty.domain.model.GardenCell as GardenCellDomain
import szysz3.planty.domain.model.GardenState as GardenStateDomain

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