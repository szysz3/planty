package szysz3.planty.screen.taskdetails.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.taskdetails.viewmodel.TaskDetailsViewModel

@Composable
fun TaskDetailsScreen(
    mainScreenViewModel: MainScreenViewModel,
    taskDetailsScreen: TaskDetailsViewModel = hiltViewModel()
) {
}