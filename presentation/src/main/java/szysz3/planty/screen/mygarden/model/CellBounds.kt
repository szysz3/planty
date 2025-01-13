package szysz3.planty.screen.mygarden.model

data class CellBounds(
    val minRow: Int,
    val maxRow: Int,
    val minCol: Int,
    val maxCol: Int
) {
    companion object {
        fun from(cells: Set<Pair<Int, Int>>): CellBounds {
            require(cells.isNotEmpty()) { "Cell set cannot be empty" }

            return CellBounds(
                minRow = cells.minOf { it.first },
                maxRow = cells.maxOf { it.first },
                minCol = cells.minOf { it.second },
                maxCol = cells.maxOf { it.second }
            )
        }
    }

    val width: Int get() = maxCol - minCol + 1
    val height: Int get() = maxRow - minRow + 1
    val area: Int get() = width * height

    fun isValidRectangularSelection(cells: Set<Pair<Int, Int>>): Boolean {
        return cells.size == area
    }

    fun toMergedCell(parentGardenId: Int): MergedCell {
        return MergedCell(
            id = 0,
            parentGardenId = parentGardenId,
            startRow = minRow,
            startColumn = minCol,
            endRow = maxRow,
            endColumn = maxCol,
            subGardenId = null
        )
    }
}

fun Set<Pair<Int, Int>>.toCellBounds(): CellBounds {
    return CellBounds.from(this)
}