package szysz3.planty.screen.mygarden.model

import szysz3.planty.core.model.GardenCell
import szysz3.planty.core.model.toDomain
import szysz3.planty.core.model.toPresentationModel
import szysz3.planty.domain.model.GardenState as GardenStateDomain

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
    return szysz3.planty.domain.model.GardenState(
        rows = this.rows,
        columns = this.columns,
        cells = this.cells.map { cell ->
            szysz3.planty.domain.model.GardenCell(
                id = cell.id,
                row = cell.row,
                column = cell.column,
                plant = cell.plant?.toDomain()
            )
        }
    )
}