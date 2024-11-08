package szysz3.planty.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import szysz3.planty.R
import szysz3.planty.ui.widgets.RoundedButton
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = hiltViewModel()) {
    var gardenDimensions by remember { mutableStateOf<MapDimensions?>(null) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    val scaffoldState = rememberBottomSheetScaffoldState()

    LaunchedEffect(isBottomSheetVisible) {
        if (isBottomSheetVisible) {
            scaffoldState.bottomSheetState.expand()
        } else {
            scaffoldState.bottomSheetState.collapse()
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            DimensionsInput(
                onDimensionsSubmitted = { dimensions ->
                    gardenDimensions = dimensions
                    isBottomSheetVisible = false
                }
            )
        },
        sheetPeekHeight = 0.dp,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            if (gardenDimensions != null) {
                GardenMap(
                    rows = gardenDimensions?.rowCount ?: 0,
                    columns = gardenDimensions?.columnCount ?: 0,
                    plants = listOf("A", "B", "C")
                ) { row, col, plant ->
                    Timber.i("GardenMap: $row, $col, $plant")
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.placeholder),
                        contentDescription = "Garden map placeholder",
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    RoundedButton(
                        onClick = { isBottomSheetVisible = true },
                        text = "Create New Map",
                    )
                }
            }
        }
    }
}