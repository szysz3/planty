package szysz3.planty.screen.plantaplant.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import szysz3.planty.R
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.plantaplant.composable.PlantCard
import szysz3.planty.screen.plantaplant.viewmodel.PlantAPlantViewModel
import szysz3.planty.screen.plantdetails.model.PlantDetailsScreenOrigin
import szysz3.planty.ui.widgets.EllipticalBackground

@Composable
fun PlantAPlantScreen(
    mainScreenViewModel: MainScreenViewModel,
    plantAPlantViewModel: PlantAPlantViewModel,
    onNavigateToPlantDetails: (PlantDetailsScreenOrigin) -> Unit
) {
    val uiState by plantAPlantViewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    // Local state for managing search query input
    val localSearchQuery = remember { mutableStateOf(uiState.searchQuery) }

    LaunchedEffect(Unit) {
        mainScreenViewModel.updateShowBackButton(true)
        mainScreenViewModel.updateShowDeleteButton(false)
    }

    EllipticalBackground(R.drawable.bcg2)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                focusManager.clearFocus()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Search Bar
            OutlinedTextField(
                value = localSearchQuery.value,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                maxLines = 1,
                onValueChange = { value ->
                    localSearchQuery.value = value
                    plantAPlantViewModel.updateSearchQuery(value)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                label = { Text("Search for a plant...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (localSearchQuery.value.isNotEmpty()) {
                        IconButton(onClick = {
                            localSearchQuery.value = ""
                            plantAPlantViewModel.updateSearchQuery("")
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = "Clear Search"
                            )
                        }
                    }
                },
            )

            // Plant Grid
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.plants.size) { index ->
                    PlantCard(
                        plant = uiState.plants[index],
                        onPlantSelected = {
                            plantAPlantViewModel.selectPlant(uiState.plants[index])
                            onNavigateToPlantDetails(PlantDetailsScreenOrigin.PLANT_A_PLANT_SCREEN)
                        }
                    )
                }
            }
        }
    }
}