package szysz3.planty.screen.main.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import szysz3.planty.screen.home.viewmodel.HomeScreenViewModel
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(mainScreenViewModel: MainScreenViewModel, homeScreenViewModel: HomeScreenViewModel) {
    val isTopBarVisible by mainScreenViewModel.isTopBarVisible.collectAsState()
    AnimatedVisibility(isTopBarVisible) {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center // Center the text within the Box
                ) {
                    Text(
                        text = "Home",
                        maxLines = 1
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    homeScreenViewModel.showDeleteDialog(true)
                }) {
                    Icon(Icons.Rounded.Delete, contentDescription = "Delete")
                }
            },
        )
    }
}