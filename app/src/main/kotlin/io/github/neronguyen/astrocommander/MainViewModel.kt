package io.github.neronguyen.astrocommander

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    val name: StateFlow<String>
        field = MutableStateFlow("Android")
}
