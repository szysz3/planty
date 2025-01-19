package szysz3.planty.screen.mygarden.model

import szysz3.planty.core.model.GardenCell
import szysz3.planty.core.model.toDomain
import szysz3.planty.core.model.toPresentationModel
import szysz3.planty.domain.model.CellRange
import szysz3.planty.domain.model.GardenCell as GardenCellDomain
import szysz3.planty.domain.model.GardenState as GardenStateDomain
import szysz3.planty.domain.model.MergedCell as MergedCellDomain

fun GardenState.toDomain(): GardenStateDomain {
    return GardenStateDomain(
        id = id,
        rows = rows,
        columns = columns,
        cells = cells.map { it.toDomain() },
        mergedCells = mergedCells.map { it.toDomain() }
    )
}

fun GardenStateDomain.toPresentationModel(): GardenState {
    return GardenState(
        id = id,
        rows = rows,
        columns = columns,
        cells = cells.map { it.toPresentation() },
        mergedCells = mergedCells.map { it.toPresentation() },
        name = name
    )
}

fun MergedCellDomain.toPresentation(): MergedCell {
    return MergedCell(
        id = id,
        parentGardenId = gardenId,
        startRow = cellRange.startRow,
        startColumn = cellRange.startColumn,
        endRow = cellRange.endRow,
        endColumn = cellRange.endColumn,
        subGardenId = subGardenId,
        subGardenName = subGardenName,
        subGardenRows = subGardenRows,
        subGardenColumns = subGardenColumns
    )
}

fun MergedCell.toDomain(): MergedCellDomain {
    return MergedCellDomain(
        id = id,
        gardenId = parentGardenId ?: 0,
        cellRange = CellRange(
            startRow = startRow,
            startColumn = startColumn,
            endRow = endRow,
            endColumn = endColumn,
        ),
        subGardenId = subGardenId
    )
}

fun GardenCell.toDomain(): GardenCellDomain {
    return szysz3.planty.domain.model.GardenCell(
        id = id,
        row = row,
        column = column,
        plant = plant?.toDomain()
    )
}

fun GardenCellDomain.toPresentation(): GardenCell {
    return GardenCell(
        id = id,
        row = row,
        column = column,
        plant = plant?.toPresentationModel()
    )
}

fun CellBounds.toDomain(): CellRange {
    return CellRange(
        startRow = minRow,
        startColumn = minCol,
        endRow = maxRow,
        endColumn = maxCol
    )
}

fun CellRange.toPresentation(): CellBounds {
    return CellBounds(
        minRow = startRow,
        maxRow = endRow,
        minCol = startColumn,
        maxCol = endColumn
    )
}