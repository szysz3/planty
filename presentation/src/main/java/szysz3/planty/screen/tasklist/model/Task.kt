package szysz3.planty.screen.tasklist.model

import androidx.compose.ui.graphics.Color
import szysz3.planty.ui.theme.EarthyBrown

data class Task(
    val id: Long = 0,
    val title: String,
    val tasks: List<SubTask>,
    val isCompleted: Boolean = false,
    val color: Color = EarthyBrown,
    val index: Int = 0,
) {
    companion object {
        fun empty(): Task {
            return Task(
                title = "",
                tasks = emptyList()
            )
        }
    }
}