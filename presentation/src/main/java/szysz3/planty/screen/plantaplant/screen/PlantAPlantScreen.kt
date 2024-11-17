package szysz3.planty.screen.plantaplant.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.plantaplant.composable.PlantCard
import szysz3.planty.screen.plantaplant.model.Plant
import szysz3.planty.screen.plantaplant.viewmodel.PlantAPlantViewModel

@Composable
fun PlantAPlantScreen(
    mainScreenViewModel: MainScreenViewModel,
    plantAPlantViewModel: PlantAPlantViewModel = hiltViewModel(),
    onNavigateToPlantDetails: () -> Unit
) {
    val plants =
        listOf(Plant.random(), Plant.random(), Plant.random(), Plant.random(), Plant.random())

    LaunchedEffect(Unit) {
        mainScreenViewModel.showBackButton(true)
        mainScreenViewModel.showDeleteButton(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = "query",
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            label = { Text("Search for a plant...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search"
                )
            }
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(plants.size) { index ->
                PlantCard(
                    plant = plants[index],
                    onPlantSelected = {
                        onNavigateToPlantDetails()
                    }
                )
            }
        }
    }
}