package szysz3.planty.screen.main.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import androidx.compose.ui.draw.alpha
import androidx.navigation.NavController
import szysz3.planty.screen.home.viewmodel.HomeScreenViewModel
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navigationController: NavController,
    mainScreenViewModel: MainScreenViewModel,
    homeScreenViewModel: HomeScreenViewModel
) {
    val isTopBarVisible by mainScreenViewModel.isTopBarVisible.collectAsState()
    val showBackButton by mainScreenViewModel.showBackButton.collectAsState()
    val showDeleteButton by mainScreenViewModel.showDeleteButton.collectAsState()

    AnimatedVisibility(
        visible = isTopBarVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Home",
                        maxLines = 1
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        mainScreenViewModel.showBackButton(false)
                        navigationController.popBackStack()
                    },
                    modifier = Modifier.alpha(if (showBackButton) 1f else 0f),
                    enabled = showBackButton
                ) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Delete")
                }

            },
            actions = {
                IconButton(
                    onClick = {
                        homeScreenViewModel.showDeleteDialog(true)
                    },
                    modifier = Modifier.alpha(if (showDeleteButton) 1f else 0f),
                    enabled = showDeleteButton
                ) {
                    Icon(Icons.Rounded.Delete, contentDescription = "Delete")
                }

            },
        )
    }
}