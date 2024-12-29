package szysz3.planty.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Home
import szysz3.planty.screen.mygarden.MyGardenFeature
import szysz3.planty.screen.tasklist.TaskListFeature

object Screens {
    val MyGarden = BottomNavItem(MyGardenFeature.ROUTE, MyGardenFeature.TITLE, Icons.Rounded.Home)
    val TaskList = BottomNavItem(TaskListFeature.ROUTE, TaskListFeature.TITLE, Icons.Rounded.Done)

}