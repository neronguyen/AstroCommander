package io.github.neronguyen.astrocommander

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.neronguyen.astrocommander.core.network.model.PlaceholderJson

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    onItemClick: (PlaceholderJson) -> Unit
) {
    val list by viewModel.list.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    HomeScreen(
        list = list,
        error = error,
        onItemClick = onItemClick,
        modifier = modifier
    )
}

@Composable
fun HomeScreen(
    list: List<PlaceholderJson>,
    error: String,
    onItemClick: (PlaceholderJson) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(top = 8.dp)
    ) {
        if (error.isNotEmpty()) {
            item {
                Text(text = error)
            }
        }

        items(list) { item ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(item) }
                    .padding(8.dp)
            ) {
                Text(
                    text = "ID: ${item.id}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = item.title)
            }
        }
    }
}
