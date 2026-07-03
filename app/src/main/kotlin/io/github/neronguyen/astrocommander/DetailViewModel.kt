package io.github.neronguyen.astrocommander

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.neronguyen.astrocommander.core.database.dao.PlaceholderJsonDao
import io.github.neronguyen.astrocommander.core.network.model.PlaceholderJson
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val placeholderJsonDao: PlaceholderJsonDao,
) : ViewModel() {

    private val _item = MutableStateFlow<PlaceholderJson?>(null)
    val item = _item.asStateFlow()

    fun setItem(item: PlaceholderJson) {
        viewModelScope.launch {
            val localItem = placeholderJsonDao.getPlaceholderJson(item.id)
            if (localItem != null) {
                _item.update { item.copy(title = "Details-${item.title}") }
            } else {
                _item.update { item }
            }
        }
    }
}
