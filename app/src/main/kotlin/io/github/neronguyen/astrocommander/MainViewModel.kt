package io.github.neronguyen.astrocommander

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    val name: StateFlow<String>
        field = MutableStateFlow("Android")
}
