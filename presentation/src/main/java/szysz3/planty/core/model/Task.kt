package szysz3.planty.core.model

import androidx.compose.ui.graphics.Color
import szysz3.planty.theme.DarkCardColors
import szysz3.planty.theme.LightCardColors

data class Task(
    val id: Long = 0,
    val title: String,
    val tasks: List<SubTask>,
    val isCompleted: Boolean = false,
    val color: Color = Color.Transparent,
    val index: Int = 0,
) {
    companion object {
        // TODO: remove color init
        fun empty(isDarkMode: Boolean): Task {
            return Task(
                title = "",
                tasks = emptyList(),
                color = if (isDarkMode) {
                    DarkCardColors.random()
                } else {
                    LightCardColors.random()
                }
            )
        }
    }
}