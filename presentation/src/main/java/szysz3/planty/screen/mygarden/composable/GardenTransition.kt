package szysz3.planty.screen.mygarden.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.runtime.Composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GardenTransition(
    targetState: Int?, // garden ID
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedContent(
        targetState = targetState,
        transitionSpec = {
            if (targetState != null && initialState != null) {
                // Going deeper into sub-garden
                // TODO: REVISIT
                if (targetState > initialState!!) {
                    slideInHorizontally { width -> width } + fadeIn() with
                            slideOutHorizontally { width -> -width } + fadeOut()
                } else {
                    // Going back up
                    slideInHorizontally { width -> -width } + fadeIn() with
                            slideOutHorizontally { width -> width } + fadeOut()
                }
            } else {
                // Default transition
                fadeIn() with fadeOut()
            }
        }
    ) { state ->
        content()
    }
}