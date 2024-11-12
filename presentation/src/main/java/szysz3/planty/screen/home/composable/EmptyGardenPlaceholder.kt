package szysz3.planty.screen.home.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import szysz3.planty.R
import szysz3.planty.ui.widgets.RoundedButton

@Composable
fun EmptyGardenPlaceholder(onCreateNewMap: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.placeholder),
            contentDescription = "Garden map placeholder",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        RoundedButton(onClick = onCreateNewMap, text = "Create New Map")
    }
}