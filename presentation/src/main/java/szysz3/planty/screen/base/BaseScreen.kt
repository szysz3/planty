package szysz3.planty.screen.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import szysz3.planty.screen.base.bottombar.BottomNavigationBar
import szysz3.planty.screen.base.topbar.TopBarActionButton
import szysz3.planty.screen.base.topbar.TopBarBackButton
import szysz3.planty.screen.base.topbar.TopBarTitle

/**
 * A base screen component that provides a common layout structure for the application screens.
 * Includes an animated top bar with customizable actions and title, and an optional bottom navigation bar.
 * The content of the screen is wrapped with entrance/exit animations.
 *
 * @param title The text to be displayed in the top bar
 * @param showTopBar Whether to show the top app bar. Defaults to true
 * @param showBottomBar Whether to show the bottom navigation bar. Defaults to true
 * @param navController Navigation controller for handling screen navigation
 * @param topBarActions Composable lambda for defining custom actions in the top bar.
 *                     Defaults to a single action button
 * @param topBarBackNavigation Composable for custom back navigation button.
 *                            Defaults to standard back button
 * @param content The main content of the screen. Receives [PaddingValues] for proper layout spacing
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    title: String,
    showTopBar: Boolean = true,
    showBottomBar: Boolean = true,
    navController: NavHostController,
    topBarActions: (@Composable RowScope.() -> Unit)? = {
        TopBarActionButton()
    },
    topBarBackNavigation: @Composable () -> Unit = {
        TopBarBackButton()
    },
    content: @Composable (PaddingValues) -> Unit
) {
    var isContentVisible by remember { mutableStateOf(false) }
    var isTitleVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isContentVisible = true
        delay(AnimationConstants.TITLE_VISIBILITY_DELAY.toLong())
        isTitleVisible = true
    }

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
                    title = {
                        AnimatedTopBarTitle(
                            title = title,
                            isVisible = isTitleVisible
                        )
                    },
                    navigationIcon = {
                        topBarBackNavigation()
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
        AnimatedContent(isContentVisible, innerPadding, content)
    }
}

private object AnimationConstants {
    const val TITLE_ANIMATION_DURATION = 300
    const val CONTENT_FADE_DURATION = 300
    const val CONTENT_SLIDE_DURATION = 150
    const val TITLE_VISIBILITY_DELAY = 150

    const val TITLE_SLIDE_DIVISOR = 3
    const val CONTENT_SLIDE_DIVISOR = 4
}

@Composable
private fun AnimatedTopBarTitle(
    title: String,
    isVisible: Boolean
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = AnimationConstants.TITLE_ANIMATION_DURATION,
                easing = LinearEasing
            )
        ) + slideInHorizontally(
            animationSpec = tween(
                durationMillis = AnimationConstants.TITLE_ANIMATION_DURATION,
                easing = FastOutSlowInEasing
            )
        ) { fullWidth -> fullWidth / AnimationConstants.TITLE_SLIDE_DIVISOR },
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = AnimationConstants.TITLE_ANIMATION_DURATION,
                easing = LinearEasing
            )
        ) + slideOutHorizontally(
            animationSpec = tween(
                durationMillis = AnimationConstants.TITLE_ANIMATION_DURATION,
                easing = FastOutSlowInEasing
            )
        ) { fullWidth -> -fullWidth / AnimationConstants.TITLE_SLIDE_DIVISOR }
    ) {
        TopBarTitle(title)
    }
}

@Composable
private fun AnimatedContent(
    isVisible: Boolean,
    innerPaddingValues: PaddingValues,
    content: @Composable (PaddingValues) -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter =
        fadeIn(animationSpec = tween(AnimationConstants.CONTENT_FADE_DURATION)) +
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth / AnimationConstants.CONTENT_SLIDE_DIVISOR },
                    animationSpec = tween(AnimationConstants.CONTENT_SLIDE_DURATION)
                ),
        exit =
        fadeOut(animationSpec = tween(AnimationConstants.CONTENT_FADE_DURATION)) +
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth / AnimationConstants.CONTENT_SLIDE_DIVISOR },
                    animationSpec = tween(AnimationConstants.CONTENT_SLIDE_DURATION)
                ),
    ) {
        content(innerPaddingValues)
    }
}