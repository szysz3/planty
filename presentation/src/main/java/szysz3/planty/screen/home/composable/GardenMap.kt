package szysz3.planty.screen.home.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import szysz3.planty.screen.home.model.GardenCell

@Composable
fun GardenMap(
    rows: Int,
    columns: Int,
    plants: List<String>,
    selectedCells: List<GardenCell>,
    onPlantSelected: (Int, Int, String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(columns / rows.toFloat())
    ) {
        items(rows * columns) { index ->
            val row = index / columns
            val col = index % columns
            val plant = selectedCells.find { it.row == row && it.column == col }?.plant
            val isSelected = !plant.isNullOrBlank()

            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .background(if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground)
                    .aspectRatio(1f)
                    .clickable {
                        val selectedPlant = plants.firstOrNull() ?: ""
//                        val selectedPlant = ""
                        onPlantSelected(row, col, selectedPlant)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = plant ?: "")
            }
        }
    }
}