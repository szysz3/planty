package szysz3.planty.screen.main.composable

import MyGardenScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import szysz3.planty.screen.imagegallery.screen.ImageGalleryScreen
import szysz3.planty.screen.main.viewmodel.MainScreenViewModel
import szysz3.planty.screen.mygarden.viewmodel.MyGardenViewModel
import szysz3.planty.screen.plantaplant.screen.PlantAPlantScreen
import szysz3.planty.screen.plantaplant.viewmodel.PlantAPlantViewModel
import szysz3.planty.screen.plantdetails.model.PlantDetailsScreenOrigin
import szysz3.planty.screen.plantdetails.screen.PlantDetailsScreen
import szysz3.planty.screen.plantid.screen.PlantIdScreen
import szysz3.planty.screen.taskdetails.screen.TaskDetailsScreen
import szysz3.planty.screen.tasklist.screen.TaskListScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    mainScreenViewModel: MainScreenViewModel,
    myGardenViewModel: MyGardenViewModel,
    plantAPlantViewModel: PlantAPlantViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Home.route) {
            MyGardenScreen(
                mainScreenViewModel = mainScreenViewModel,
                myGardenViewModel = myGardenViewModel,
                onNavigateToPlantAPlant = {
                    navigate(navController, NavigationItem.PlantAPlant)
                },
                onNavigateToPlantDetails = { origin, plantId ->
                    navigate(
                        navController,
                        NavigationItem.PlantDetails.withArgs(origin.value, plantId)
                    )
                }
            )
        }
        composable(BottomNavItem.TaskList.route) {
            TaskListScreen(
                mainScreenViewModel = mainScreenViewModel
            ) {
                navigate(navController, NavigationItem.TaskDetails)
            }
        }
        composable(BottomNavItem.PlantId.route) {
            PlantIdScreen(mainScreenViewModel = mainScreenViewModel) { localMatchingPlant ->
                localMatchingPlant?.let {
                    navigate(
                        navController,
                        NavigationItem.PlantDetails.withArgs(
                            PlantDetailsScreenOrigin.PLANT_ID_SCREEN.value,
                            localMatchingPlant.id
                        )
                    )
                }
            }
        }
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
                mainScreenViewModel = mainScreenViewModel,
                myGardenViewModel = myGardenViewModel,
                origin = PlantDetailsScreenOrigin.fromValue(origin),
                onPlantChosen = {
                    navController.popBackStack(BottomNavItem.Home.route, false)
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
        composable(NavigationItem.PlantAPlant.route) {
            PlantAPlantScreen(
                mainScreenViewModel = mainScreenViewModel,
                plantAPlantViewModel = plantAPlantViewModel,
            ) { origin, plantId ->
                navigate(navController, NavigationItem.PlantDetails.withArgs(origin.value, plantId))
            }
        }

        composable(NavigationItem.TaskDetails.route) {
            TaskDetailsScreen(
                mainScreenViewModel = mainScreenViewModel,
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
    object TaskDetails : NavigationItem("/taskList/taskDetails", "Task details")
    object PlantAPlant : NavigationItem("/myGarden/plantAPlant", "Plant a plant")
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
                route == PlantAPlant.route -> PlantAPlant.title
                route == BottomNavItem.Home.route -> BottomNavItem.Home.title
                route == BottomNavItem.TaskList.route -> BottomNavItem.TaskList.title
                route == BottomNavItem.PlantId.route -> BottomNavItem.PlantId.title
                else -> null
            }
        }
    }
}

open class BottomNavItem(route: String, title: String, val icon: ImageVector) :
    NavigationItem(route, title) {
    object Home : BottomNavItem("myGarden", "My Garden", Icons.Rounded.Home)
    object TaskList : BottomNavItem("taskList", "Tasks", Icons.Rounded.Done)
    object PlantId :
        BottomNavItem("plantId", "Id", Icons.Rounded.Search)
}

