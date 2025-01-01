package szysz3.planty.navigation

/**
 * A simple interface that enforces a uniform API across all features.
 */
interface FeatureRoute {

    /**
     * The relative base path for a feature (no placeholders).
     * Example: "/plantDetails"
     */
    val basePath: String

    /**
     * The path that includes placeholders for arguments.
     * Example: "/plantDetails?plantId={plantId}&config={config}&..."
     *
     * This is what's actually registered in the NavGraph builder,
     * letting Navigation parse the arguments automatically.
     */
    val routeWithArgsPattern: String

    /**
     * Builds a route string for the base path without any arguments.
     * Must remain accessible externally (e.g. from deep links or direct calls).
     */
    fun baseRoute(origin: String = ""): String = "$origin$basePath"

    /**
     * Builds a route string that includes placeholders
     * (used as the route in `composable(route = ...)`).
     */
    fun route(origin: String = ""): String = "$origin$routeWithArgsPattern"
}