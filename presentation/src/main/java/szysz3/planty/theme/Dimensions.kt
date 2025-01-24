package szysz3.planty.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

class Dimensions {
    val elevation4 = 4.dp
    val elevation8 = 8.dp

    val spacing2 = 2.dp
    val spacing4 = 4.dp
    val spacing8 = 8.dp
    val spacing12 = 12.dp
    val spacing16 = 16.dp
    val spacing20 = 20.dp
    val spacing24 = 24.dp
    val spacing32 = 32.dp
    val spacing36 = 36.dp
    val spacing48 = 48.dp

    val corner2 = 2.dp
    val corner4 = 4.dp
    val corner8 = 8.dp
    val corner12 = 12.dp
    val corner16 = 16.dp

    val size1 = 1.dp
    val size2 = 2.dp
    val size4 = 4.dp
    val size8 = 8.dp
    val size12 = 12.dp
    val size16 = 16.dp
    val size20 = 20.dp
    val size24 = 24.dp
    val size32 = 32.dp
    val size36 = 36.dp
    val size48 = 48.dp
    val size64 = 64.dp

}

@Composable
fun MaterialTheme.dimensions(): Dimensions {
    return LocalDimensions.current
}

private val LocalDimensions = staticCompositionLocalOf { Dimensions() }