package szysz3.planty.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = hiltViewModel()) {

    val sampleData by viewModel.sampleData.collectAsState()
    viewModel.getSampleData()

    Text(
        text = "Home screen: ${sampleData?.text}",
        modifier = Modifier.fillMaxSize(),
        textAlign = TextAlign.Center
    )
}