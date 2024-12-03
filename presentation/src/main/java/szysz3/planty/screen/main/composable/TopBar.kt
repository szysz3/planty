package szysz3.planty.screen.main.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.mygarden.viewmodel.MyGardenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navigationController: NavController,
    mainScreenViewModel: MainScreenViewModel,
    myGardenViewModel: MyGardenViewModel
) {
    val uiState by mainScreenViewModel.uiState.collectAsState()
    val currentBackStackEntry by navigationController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val title = remember(currentRoute) {
        NavigationItem.getTitleForRoute(currentRoute ?: "") ?: ""
    }

    AnimatedVisibility(
        visible = uiState.isTopBarVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        TopAppBar(
            title = { TopBarTitle(title) },
            navigationIcon = {
                BackButton(
                    showBackButton = uiState.showBackButton,
                    onBackClick = {
                        mainScreenViewModel.updateShowBackButton(false)
                        navigationController.popBackStack()
                    }
                )
            },
            actions = {
                DeleteButton(
                    showDeleteButton = uiState.showDeleteButton,
                    onDeleteClick = { myGardenViewModel.showDeleteDialog(true) }
                )
            }
        )
    }
}

@Composable
fun BackButton(
    showBackButton: Boolean,
    onBackClick: () -> Unit
) {
    // Reserve space for the back button even when it's not visible
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(48.dp), // Match the size of the IconButton
        contentAlignment = Alignment.Center
    ) {
        if (showBackButton) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
            }
        }
    }
}

@Composable
fun DeleteButton(
    showDeleteButton: Boolean,
    onDeleteClick: () -> Unit
) {
    // Reserve space for the delete button even when it's not visible
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(48.dp), // Match the size of the IconButton
        contentAlignment = Alignment.Center
    ) {
        if (showDeleteButton) {
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Rounded.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun TopBarTitle(title: String) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            maxLines = 1
        )
    }
}