package szysz3.planty.screen.plantcatalog.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import szysz3.planty.R
import szysz3.planty.core.composable.EllipticalBackground
import szysz3.planty.core.model.PlantCatalogConfig
import szysz3.planty.screen.base.BaseScreen
import szysz3.planty.screen.base.topbar.TopBarBackButton
import szysz3.planty.screen.plantcatalog.composable.PlantCard
import szysz3.planty.screen.plantcatalog.composable.SearchBar
import szysz3.planty.screen.plantcatalog.viewmodel.PlantCatalogViewModel

@Composable
fun PlantCatalogScreen(
    title: String,
    navController: NavHostController,
    origin: PlantCatalogConfig,
    row: Int?,
    column: Int?,
    gardenId: Int?,
    onShowPlantDetails: (plantId: Int, row: Int?, column: Int?, gardenId: Int?) -> Unit,
    plantCatalogViewModel: PlantCatalogViewModel = hiltViewModel(),
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    val plants = plantCatalogViewModel.pagedPlants.collectAsLazyPagingItems()
    val searchQuery by plantCatalogViewModel.searchQuery.collectAsState()

    BaseScreen(
        title = title,
        showTopBar = true,
        showBottomBar = true,
        topBarBackNavigation = {
            TopBarBackButton(
                showBackButton = origin == PlantCatalogConfig.PLANT,
                onBackClick = { navController.popBackStack() }
            )
        },
        navController = navController
    ) { padding ->
        EllipticalBackground(R.drawable.bcg2)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    focusManager.clearFocus()
                },
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                SearchBar(
                    query = searchQuery,
                    onQueryChange = { newValue ->
                        plantCatalogViewModel.updateSearchQuery(newValue)
                    },
                    onClearQuery = {
                        plantCatalogViewModel.updateSearchQuery("")
                    }
                )

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    when (plants.loadState.refresh) {
                        is LoadState.Loading -> {
                            items(10) {
                                PlantCard(modifier = Modifier.alpha(0.7f))
                            }
                        }

                        else -> {
                            items(
                                count = plants.itemCount,
                                key = { index -> plants[index]?.id ?: index }
                            ) { index ->
                                val plant = plants[index]
                                PlantCard(
                                    plant = plant,
                                    onPlantSelected = {
                                        plant?.let {
                                            onShowPlantDetails(it.id, row, column, gardenId)
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
