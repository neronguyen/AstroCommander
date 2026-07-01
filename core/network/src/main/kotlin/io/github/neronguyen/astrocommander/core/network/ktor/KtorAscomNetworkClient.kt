package io.github.neronguyen.astrocommander.core.network.ktor

import arrow.core.Either
import arrow.core.raise.either
import io.github.neronguyen.astrocommander.core.network.AscomNetworkDataSource

internal class KtorAscomNetworkClient(
    val httpClient: KtorClient
) : AscomNetworkDataSource {

    override suspend fun getPlaceholderJson(): String {
        val result = either {
            httpClient.get<String>("todos/1")
        }

        return when (result) {
            is Either.Left -> "Get JSON failed"
            is Either.Right -> result.value
        }
    }
}
