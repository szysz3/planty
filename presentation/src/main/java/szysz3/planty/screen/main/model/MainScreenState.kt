package szysz3.planty.screen.main.model

data class MainScreenState(
    val isHomeScreenInitialized: Boolean = false,
    val isTopBarVisible: Boolean = false,
    val showBackButton: Boolean = false,
    val showDeleteButton: Boolean = false
)