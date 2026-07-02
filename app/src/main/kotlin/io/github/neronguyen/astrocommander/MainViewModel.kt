package io.github.neronguyen.astrocommander

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.raise.context.either
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.neronguyen.astrocommander.core.database.dao.PlaceholderJsonDao
import io.github.neronguyen.astrocommander.core.database.model.PlaceholderJsonEntity
import io.github.neronguyen.astrocommander.core.network.AscomNetworkDataSource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkDataSource: AscomNetworkDataSource,
    private val placeholderJsonDao: PlaceholderJsonDao,
) : ViewModel() {

    init {
        viewModelScope.launch {
            either {
                networkDataSource.getPlaceholderJson()
            }.onLeft { error ->
                name.update { "Error: $error" }
            }.onRight { result ->
                val entity = PlaceholderJsonEntity(
                    id = result.id,
                    title = result.title,
                    completed = result.completed
                )
                placeholderJsonDao.upsertPlaceholderJson(entity)
            }
        }

        viewModelScope.launch {
            placeholderJsonDao.observePlaceholderJson().collect { entity ->
                if (entity != null) {
                    name.update { "${entity.id}\n${entity.title}" }
                }
            }
        }
    }

    val name: StateFlow<String>
        field = MutableStateFlow("Android")
}
