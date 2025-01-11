package szysz3.planty.screen.mygarden.model

import szysz3.planty.core.model.GardenCell
import szysz3.planty.core.model.toDomain
import szysz3.planty.core.model.toPresentationModel
import timber.log.Timber
import szysz3.planty.domain.model.GardenCell as GardenCellDomain
import szysz3.planty.domain.model.GardenState as GardenStateDomain
import szysz3.planty.domain.model.MergedCell as MergedCellDomain

fun MergedCellDomain.toPresentationModel(): MergedCell {
    return MergedCell(
        id = this.id,
        parentGardenId = this.parentGardenId,
        startRow = this.startRow,
        startColumn = this.startColumn,
        endRow = this.endRow,
        endColumn = this.endColumn,
        subGardenId = this.subGardenId
    )
}

fun MergedCell.toDomainModel(): MergedCellDomain {
    Timber.d("Converting merged cell to domain: $this")
    return MergedCellDomain(
        id = this.id,
        parentGardenId = this.parentGardenId,
        startRow = this.startRow,
        startColumn = this.startColumn,
        endRow = this.endRow,
        endColumn = this.endColumn,
        subGardenId = this.subGardenId
    )
}

fun GardenStateDomain.toPresentationModel(): GardenState {
    Timber.d("Converting domain state to presentation: rows=$rows, cols=$columns, mergedCells=$mergedCells")
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
        },
        mergedCells = this.mergedCells.map { it.toPresentationModel() }
    ).also {
        Timber.d("Converted to presentation model: rows=${it.rows}, cols=${it.columns}, mergedCells=${it.mergedCells}")
    }
}

fun GardenState.toDomainModel(): GardenStateDomain {
    Timber.d("Converting to domain model. Merged cells: $mergedCells")
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
        },
        mergedCells = this.mergedCells.map { it.toDomainModel() }
    ).also {
        Timber.d("Converted to domain model. Merged cells: ${it.mergedCells}")
    }
}