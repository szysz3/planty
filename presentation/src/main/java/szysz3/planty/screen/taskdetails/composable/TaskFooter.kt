package szysz3.planty.screen.taskdetails.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import szysz3.planty.core.composable.RoundedButton
import szysz3.planty.screen.taskdetails.model.TaskDetailsScreenState

/**
 * Footer component displaying action button and cost summary for a task.
 *
 * @param state Current state of the task details screen
 * @param onButtonClick Callback invoked when the action button is clicked
 */
@Composable
fun TaskFooter(
    state: TaskDetailsScreenState,
    onButtonClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoundedButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            onClick = onButtonClick,
            text = if (state.taskId == null) "Add" else "Update"
        )

        Spacer(modifier = Modifier.weight(1f))

        if (state.totalCost > 0) {
            CostDisplay(
                completedCost = state.completedSubTaskCost,
                totalCost = state.totalCost,
            )
        }
    }
}

@Composable
private fun CostDisplay(
    completedCost: Double,
    totalCost: Double,
) {
    Column(
        modifier = Modifier.padding(end = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = String.format("%.2f", completedCost),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
        )
        Text(
            text = " out of ",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            textAlign = TextAlign.Center
        )
        Text(
            text = String.format("%.2f", totalCost),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
}