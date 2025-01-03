package szysz3.planty.screen.plantdetails.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import szysz3.planty.core.model.Plant

@Composable
fun PlantNameSection(plant: Plant) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = plant.latinName,
            style = MaterialTheme.typography.headlineMedium
        )

        plant.commonName?.let { commonName ->
            Text(
                text = "($commonName)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}