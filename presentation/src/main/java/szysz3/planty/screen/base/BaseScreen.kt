package szysz3.planty.screen.base

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import szysz3.planty.screen.base.topbar.TopBarTitle
import szysz3.planty.screen.main.composable.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    title: String,
    showTopBar: Boolean = true,
    showBottomBar: Boolean = true,
    navController: NavHostController,
    topBarActions: (@Composable RowScope.() -> Unit)? = null,
    navigationIcon: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            if (showTopBar) {
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
                        navigationIcon.invoke()
                    },
                    actions = {
                        topBarActions?.invoke(this)
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}