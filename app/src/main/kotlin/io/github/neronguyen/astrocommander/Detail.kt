package io.github.neronguyen.astrocommander

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.neronguyen.astrocommander.core.network.model.PlaceholderJson

@Composable
fun DetailRoute(
    item: PlaceholderJson,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val detailItem by viewModel.item.collectAsStateWithLifecycle()

    LaunchedEffect(item) {
        viewModel.setItem(item)
    }

    detailItem?.let {
        DetailScreen(item = it, modifier = modifier, onBack = onBack)
    }
}

@Composable
fun DetailScreen(
    item: PlaceholderJson,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Button(onClick = onBack) {
            Text("Back")
        }
        Text(
            text = "Details",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "ID: ${item.id}",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Title: ${item.title}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Completed: ${item.completed}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
