package io.github.neronguyen.astrocommander

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.raise.context.either
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.neronguyen.astrocommander.core.network.AscomNetworkDataSource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkDataSource: AscomNetworkDataSource
) : ViewModel() {

    init {
        viewModelScope.launch {
            either {
                networkDataSource.getPlaceholderJson()
            }.onLeft { error ->
                name.update { "Error: $error" }
            }.onRight { result ->
                name.update { result.title }
            }
        }
    }

    val name: StateFlow<String>
        field = MutableStateFlow("Android")
}
