package szysz3.planty.screen.plantcatalog.screen

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import szysz3.planty.R
import szysz3.planty.screen.base.BaseScreen
import szysz3.planty.screen.plantcatalog.composable.PlantCard
import szysz3.planty.screen.plantcatalog.viewmodel.PlantCatalogViewModel
import szysz3.planty.screen.plantdetails.model.PlantDetailsScreenOrigin
import szysz3.planty.ui.widgets.EllipticalBackground

@Composable
fun PlantCatalogScreen(
    title: String,
    navController: NavHostController,
    onShowPlantDetails: (origin: PlantDetailsScreenOrigin, plantId: Int) -> Unit,
    plantCatalogViewModel: PlantCatalogViewModel = hiltViewModel(),
) {
    val focusManager = LocalFocusManager.current
    val plants = plantCatalogViewModel.pagedPlants.collectAsLazyPagingItems()
    val localSearchQuery =
        remember { mutableStateOf(plantCatalogViewModel.searchQuery.value) }

    EllipticalBackground(R.drawable.bcg2)

    BaseScreen(
        title = title,
        showTopBar = true,
        showBottomBar = true,
        navController = navController
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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
                        imeAction = ImeAction.Done,
                    ),
                    maxLines = 1,
                    onValueChange = { value ->
                        localSearchQuery.value = value
                        plantCatalogViewModel.updateSearchQuery(value)
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
                                plantCatalogViewModel.updateSearchQuery("")
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
                    plants.apply {
                        when (loadState.refresh) {
                            is LoadState.Loading -> {
                                items(10) {
                                    PlantCard(modifier = Modifier.alpha(0.7f))
                                }
                            }

                            else -> {
                                items(plants.itemCount) { index ->
                                    val plant = plants[index]
                                    PlantCard(
                                        plant = plant,
                                        onPlantSelected = {
                                            plant?.let {
                                                plantCatalogViewModel.selectPlant(plant)
                                                onShowPlantDetails(
                                                    PlantDetailsScreenOrigin.PLANT_A_PLANT_SCREEN,
                                                    plant.id
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}