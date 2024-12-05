package szysz3.planty.screen.plantid.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PlantResultCard(plantName: String, confidence: Double) {
    Card(
        modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = plantName, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "Confidence: ${"%.2f".format(confidence * 100)}%",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}