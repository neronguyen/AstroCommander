package io.github.neronguyen.astrocommander

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.raise.context.either
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.neronguyen.astrocommander.core.database.dao.PlaceholderJsonDao
import io.github.neronguyen.astrocommander.core.network.AscomNetworkDataSource
import io.github.neronguyen.astrocommander.core.network.model.PlaceholderJson
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkDataSource: AscomNetworkDataSource,
    private val placeholderJsonDao: PlaceholderJsonDao,
) : ViewModel() {

    private val _error = MutableStateFlow("Loading...")
    val error = _error.asStateFlow()

    private val _list = MutableStateFlow(emptyList<PlaceholderJson>())
    val list = _list.asStateFlow()

    init {
        viewModelScope.launch {
            either {
                val networkList = networkDataSource.getPlaceholderJsonList()
                val localIdSet = placeholderJsonDao
                    .observePlaceholderJsonList().first()
                    .mapTo(HashSet()) { it.id }

                networkList.map {
                    if (it.id in localIdSet) it.copy(title = "WorkManager ${it.title}")
                    else it
                }
            }.onLeft { error ->
                _error.update { "Error: $error" }
            }.onRight { result ->
                _error.update { "" }
                _list.update { result }
            }
        }
    }
}
