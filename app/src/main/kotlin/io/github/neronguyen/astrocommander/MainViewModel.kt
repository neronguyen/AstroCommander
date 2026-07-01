package io.github.neronguyen.astrocommander

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            val json = networkDataSource.getPlaceholderJson()
            name.update { json }
        }
    }

    val name: StateFlow<String>
        field = MutableStateFlow("Android")
}
