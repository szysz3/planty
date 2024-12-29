package szysz3.planty.screen.main.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import szysz3.planty.screen.base.topbar.TopBarTitle
import szysz3.planty.screen.mygarden.viewmodel.MyGardenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navigationController: NavController,
    myGardenViewModel: MyGardenViewModel
) {
    val currentBackStackEntry by navigationController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val title = remember(currentRoute) {
        NavigationItem.getTitleForRoute(currentRoute ?: "") ?: ""
    }
    TopAppBar(
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        ),
        title = { TopBarTitle(title) },
        navigationIcon = {

        },
        actions = {

        }
    )


}


