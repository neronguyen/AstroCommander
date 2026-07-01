package io.github.neronguyen.astrocommander.core.network.ktor

import io.github.neronguyen.astrocommander.core.network.AscomNetworkDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import jakarta.inject.Inject
import javax.inject.Singleton

internal class KtorAscomNetworkClient(
    val httpClient: HttpClient
) : AscomNetworkDataSource {

    override suspend fun getPlaceholderJson(): String {
        return runCatching {
            httpClient
                .get("https://jsonplaceholder.typicode.com/todos/1")
                .bodyAsText()
        }.getOrDefault("Get JSON failed")
    }
}
