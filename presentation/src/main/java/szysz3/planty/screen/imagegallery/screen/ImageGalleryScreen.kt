package szysz3.planty.screen.imagegallery.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import szysz3.planty.screen.base.BaseScreen
import szysz3.planty.screen.imagegallery.composable.ZoomableImage
import szysz3.planty.screen.imagegallery.viewmodel.ImageGalleryViewModel

@Composable
fun ImageGalleryScreen(
    title: String,
    navController: NavHostController,
    plantId: Int,
    imageGalleryViewModel: ImageGalleryViewModel = hiltViewModel(),
) {
    val uiState by imageGalleryViewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(initialPage = 0) {
        uiState.imageUrls.size
    }

    LaunchedEffect(plantId) {
        imageGalleryViewModel.updateImageUrls(plantId)
    }

    BaseScreen(
        title = title,
        showTopBar = false,
        showBottomBar = true,
        navController = navController
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.imageUrls.isNotEmpty()) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                ) { page ->
                    ZoomableImage(
                        imageUrl = uiState.imageUrls[page]
                    )
                }
            }

            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
