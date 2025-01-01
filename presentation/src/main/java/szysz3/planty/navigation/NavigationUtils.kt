package szysz3.planty.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

/**
 * Builds a query string from the provided key-value pairs.
 *
 * Example:
 *   buildQueryString("plantId" to 12, "row" to 3, "column" to null)
 * gives you: "?plantId=12&row=3"
 */
fun buildQueryString(vararg params: Pair<String, Any?>): String {
    val filtered = params
        .filter { it.second != null }.joinToString("&") { "${it.first}=${it.second}" }

    return if (filtered.isNotEmpty()) {
        "?$filtered"
    } else {
        ""
    }
}

/**
 * Retrieves a required integer argument from the back stack.
 * Throws an IllegalArgumentException if it's not present or not a valid Int.
 */
fun NavBackStackEntry.requiredIntArg(name: String): Int {
    val value = arguments?.getString(name)?.toIntOrNull()
        ?: throw IllegalArgumentException("Argument '$name' is required and must not be null")
    return value
}

/**
 * Retrieves an optional integer argument from the back stack.
 * Returns null if missing or invalid.
 */
fun NavBackStackEntry.optionalIntArg(name: String): Int? {
    return arguments?.getString(name)?.toIntOrNull()
}

fun NavGraphBuilder.staticComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = {
        fadeIn(animationSpec = tween(0))
    },
    exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        fadeOut(animationSpec = tween(durationMillis = 0))
    },
    popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = {
        fadeIn(animationSpec = tween(0))
    },
    popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        fadeOut(animationSpec = tween(durationMillis = 0))
    },
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        content = content
    )
}

fun NavHostController.showScreen(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState = false
    }
}