package szysz3.planty.screen.plantcatalog.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import szysz3.planty.R
import szysz3.planty.theme.dimensions

/**
 * A search bar component that allows users to search for plants.
 *
 * @param query Current search query text
 * @param onQueryChange Callback invoked when the search query changes
 * @param onClearQuery Callback invoked when the clear button is clicked
 * @param modifier Optional modifier for the search bar
 */
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    modifier: Modifier = Modifier
) {
    val searchBarModifier = modifier
        .fillMaxWidth()
        .padding(
            horizontal = MaterialTheme.dimensions().spacing16,
            vertical = MaterialTheme.dimensions().spacing8
        )

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        maxLines = 1,
        modifier = searchBarModifier,
        label = { Text(stringResource(R.string.search_bar_hint)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = stringResource(R.string.search_icon_description)
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearQuery) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = stringResource(R.string.clear_search_icon_description)
                    )
                }
            }
        }
    )
}