package szysz3.planty.screen.main.composable

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import szysz3.planty.screen.base.topbar.TopBarBackButton
import szysz3.planty.screen.base.topbar.TopBarDeleteButton
import szysz3.planty.screen.base.topbar.TopBarTitle
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

    val alpha by animateFloatAsState(
        targetValue = if (uiState.isTopBarVisible) 1f else 0f, label = ""
    )
    TopAppBar(
        modifier = Modifier
            .alpha(alpha),
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        ),
        title = { TopBarTitle(title) },
        navigationIcon = {
            TopBarBackButton(
                showBackButton = uiState.showBackButton,
                onBackClick = {
                    mainScreenViewModel.updateShowBackButton(false)
                    navigationController.popBackStack()
                }
            )
        },
        actions = {
            TopBarDeleteButton(
                showDeleteButton = uiState.showDeleteButton,
                onDeleteClick = { myGardenViewModel.showDeleteDialog(true) }
            )
        }
    )


}


