package io.github.neronguyen.astrocommander.core.network

interface AscomNetworkDataSource {
    suspend fun getPlaceholderJson(): String
}
