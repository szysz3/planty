package szysz3.planty.screen.tasklist.model

import androidx.compose.ui.graphics.Color

data class TaskCardUiState(
    val title: String,
    val color: Color,
    val completedSubTasks: Int,
    val totalSubTasks: Int,
    val completedCost: Double,
    val totalCost: Double,
    val isCompleted: Boolean
)