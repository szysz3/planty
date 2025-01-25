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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import szysz3.planty.R
import szysz3.planty.core.composable.RoundedButton
import szysz3.planty.screen.taskdetails.model.TaskDetailsScreenState
import szysz3.planty.theme.dimensions

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
            text = if (state.taskId == null) stringResource(R.string.task_action_add) else stringResource(
                R.string.task_action_update
            )
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
        modifier = Modifier.padding(
            end = MaterialTheme.dimensions().spacing16,
            bottom = MaterialTheme.dimensions().spacing16
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = String.format(stringResource(R.string.task_cost_format), completedCost),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.task_cost_out_of),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            textAlign = TextAlign.Center
        )
        Text(
            text = String.format(stringResource(R.string.task_cost_format), totalCost),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
}