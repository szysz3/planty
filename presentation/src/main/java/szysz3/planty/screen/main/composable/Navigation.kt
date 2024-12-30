package szysz3.planty.screen.main.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import szysz3.planty.navigation.BottomBarNavigation
import szysz3.planty.navigation.showScreen
import szysz3.planty.screen.imagegallery.screen.ImageGalleryScreen
import szysz3.planty.screen.mygarden.addMyGardenScreen
import szysz3.planty.screen.plantcatalog.screen.PlantCatalogScreen
import szysz3.planty.screen.plantdetails.model.PlantDetailsScreenOrigin
import szysz3.planty.screen.plantdetails.screen.PlantDetailsScreen
import szysz3.planty.screen.plantid.addPlantIdScreen
import szysz3.planty.screen.taskdetails.TaskDetailsFeature
import szysz3.planty.screen.taskdetails.addTaskDetailsScreen
import szysz3.planty.screen.tasklist.addTaskListScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarNavigation.MyGarden.route,
        modifier = modifier
    ) {
        addMyGardenScreen(navController)
        addTaskListScreen(
            navController,
            onShowTaskDetails = { taskId ->
                navController.showScreen(TaskDetailsFeature.routeWithArgs(taskId))
            },
            onAddNewTask = {
                navController.showScreen(TaskDetailsFeature.routeWithArgs(null))
            }
        )
        addTaskDetailsScreen(navController)
        addPlantIdScreen(navController, onShowPlantDetails = { plantId ->

        })

        composable(BottomNavItem.Catalog.route) {
            PlantCatalogScreen(
            ) { origin, plantId ->
                navigate(navController, NavigationItem.PlantDetails.withArgs(origin.value, plantId))
            }
        }

//        composable(BottomNavItem.PlantId.route) {
//            PlantIdScreen() { localMatchingPlant ->
//                localMatchingPlant?.let {
//                    navigate(
//                        navController,
//                        NavigationItem.PlantDetails.withArgs(
//                            PlantDetailsScreenOrigin.PLANT_ID_SCREEN.value,
//                            localMatchingPlant.id
//                        )
//                    )
//                }
//            }
//        }

        composable(
            route = NavigationItem.ImageGallery.route,
            arguments = listOf(
                navArgument(NavigationItem.IMAGE_GALLERY_PLANT_ID_ARG_NAME) {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val plantId =
                backStackEntry.arguments?.getInt(NavigationItem.IMAGE_GALLERY_PLANT_ID_ARG_NAME)
                    ?: -1
            ImageGalleryScreen(plantId = plantId) {
                // on close action
            }
        }

        composable(
            route = NavigationItem.PlantDetails.route,
            arguments = listOf(
                navArgument(NavigationItem.PLANT_DETAILS_ORIGIN_ARG_NAME) {
                    type = NavType.IntType
                    nullable = false
                },
                navArgument(NavigationItem.PLANT_DETAILS_PLANT_ID_ARG_NAME) {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val origin =
                backStackEntry.arguments?.getInt(NavigationItem.PLANT_DETAILS_ORIGIN_ARG_NAME) ?: -1
            val plantId =
                backStackEntry.arguments?.getInt(NavigationItem.PLANT_DETAILS_PLANT_ID_ARG_NAME)
                    ?: -1
            PlantDetailsScreen(
                origin = PlantDetailsScreenOrigin.fromValue(origin),
                onPlantChosen = {
                    navController.popBackStack(BottomBarNavigation.MyGarden.route, false)
                },
                plantId = plantId,
                onPlantImageClicked = { plantId ->
                    navigate(navController, NavigationItem.ImageGallery.withArgs(plantId))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

fun navigate(navController: NavHostController, navigationItem: NavigationItem) {
    navController.navigate(navigationItem.route) {
        launchSingleTop = true
        restoreState = true
    }
}

open class NavigationItem(val route: String, val title: String) {

    //    object PlantCatalog : NavigationItem("/myGarden/plantCatalog", "Plant catalog")
    object PlantDetails :
        NavigationItem(
            "/plantDetails/{${PLANT_DETAILS_ORIGIN_ARG_NAME}}/{${PLANT_DETAILS_PLANT_ID_ARG_NAME}}",
            "Plant details"
        ) {
        fun withArgs(origin: Int, plantId: Int): NavigationItem {
            return NavigationItem(
                route.replace(
                    "{${PLANT_DETAILS_ORIGIN_ARG_NAME}}",
                    origin.toString()
                ).replace(
                    "{${PLANT_DETAILS_PLANT_ID_ARG_NAME}}",
                    plantId.toString()
                ),

                title
            )
        }
    }

    object ImageGallery :
        NavigationItem(
            "/imageGallery/{${IMAGE_GALLERY_PLANT_ID_ARG_NAME}}",
            "Image gallery"
        ) {
        fun withArgs(plantId: Int): NavigationItem {
            return NavigationItem(
                route.replace(
                    "{${IMAGE_GALLERY_PLANT_ID_ARG_NAME}}",
                    plantId.toString()
                ),
                title
            )
        }
    }

    companion object {
        const val PLANT_DETAILS_ORIGIN_ARG_NAME = "origin"
        const val PLANT_DETAILS_PLANT_ID_ARG_NAME = "plantId"
        const val IMAGE_GALLERY_PLANT_ID_ARG_NAME = "plantId"

        fun getTitleForRoute(route: String): String? {
            return when {
                route.startsWith(PlantDetails.route.substringBefore("/{")) -> PlantDetails.title
                route == BottomNavItem.Catalog.route -> BottomNavItem.Catalog.title
//                route == BottomNavItem.PlantId.route -> BottomNavItem.PlantId.title
                else -> null
            }
        }
    }
}

open class BottomNavItem(route: String, title: String, val icon: ImageVector) :
    NavigationItem(route, title) {
    object Catalog : BottomNavItem("catalog", "Catalog", Icons.Rounded.Info)
//    object PlantId :
//        BottomNavItem("plantId", "Id", Icons.Rounded.Search)
}

