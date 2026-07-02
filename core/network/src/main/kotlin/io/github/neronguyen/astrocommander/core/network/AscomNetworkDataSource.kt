package io.github.neronguyen.astrocommander.core.network

import arrow.core.raise.Raise
import io.github.neronguyen.astrocommander.core.model.error.DataError

interface AscomNetworkDataSource {

    context(raise: Raise<DataError.Network>)
    suspend fun getPlaceholderJson(): String
}
