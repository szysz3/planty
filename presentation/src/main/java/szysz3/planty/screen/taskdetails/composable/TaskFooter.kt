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

@Composable
fun TaskFooter(
    state: TaskDetailsScreenState,
    onButtonClick: () -> Unit,

    ) {
    Row(modifier = Modifier.fillMaxWidth()) {
        RoundedButton(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .align(Alignment.Bottom),

            onClick = onButtonClick,
            text = if (state.taskId == null) "Add" else "Update"
        )

        Spacer(modifier = Modifier.weight(1f))

        if (state.totalCost > 0) {
            Column(
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(end = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    text = String.format(
                        "%.2f", state.completedSubTaskCost
                    ),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = " out of ",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = String.format(
                        "%.2f",
                        state.totalCost
                    ),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}