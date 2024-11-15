package szysz3.planty.screen.plantdetails.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel

@Composable
fun PlantDetailsScreen(
    mainScreenViewModel: MainScreenViewModel
) {
    LaunchedEffect(Unit) {
        mainScreenViewModel.showBackButton(true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        Text("PlantDetailsScreen")
    }
}