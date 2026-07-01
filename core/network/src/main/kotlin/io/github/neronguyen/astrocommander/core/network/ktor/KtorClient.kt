package io.github.neronguyen.astrocommander.core.network.ktor

import android.util.Log
import arrow.core.raise.Raise
import io.github.neronguyen.astrocommander.core.model.error.DataError
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

internal class KtorClient(private val httpClient: HttpClient) {

    context(_: Raise<DataError.Network>)
    suspend inline fun <reified T> get(
        route: String,
        queryParams: Map<String, Any> = emptyMap()
    ): T = safeCall {
        httpClient.get {
            prepareRequest(
                route = route,
                queryParams = queryParams
            )
        }
    }

    context(raise: Raise<DataError.Network>)
    private suspend inline fun <reified T> safeCall(
        execute: suspend () -> HttpResponse
    ): T {
        try {
            val response = execute()
            if (!response.status.isSuccess()) {
                raise.raise(DataError.Network.Unknown)
            }

            return response.body()

        } catch (_: SerializationException) {
            raise.raise(DataError.Network.Serialization)
        } catch (_: NoTransformationFoundException) {
            raise.raise(DataError.Network.Serialization)
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            Log.e(TAG, "safeCall: ", e)
            raise.raise(e.asNetworkError())
        }
    }

    private fun HttpRequestBuilder.prepareRequest(
        route: String,
        queryParams: Map<String, Any>,
    ) {
        url(constructRoute(route))
        queryParams.forEach { (key, value) -> parameter(key, value) }
    }

    private fun constructRoute(route: String): String {
        // TODO
        val rawBaseUrl = "https://jsonplaceholder.typicode.com/"
        if (route.contains(rawBaseUrl)) {
            return route
        }

        val sanitizedRoute = route.removePrefix("/")
        val baseUrl = rawBaseUrl.removeSuffix("/") + "/"
        return "$baseUrl$sanitizedRoute"
    }

    private fun Exception.asNetworkError(): DataError.Network = when (this) {
        is UnknownHostException,
        is UnresolvedAddressException,
        is ConnectException -> DataError.Network.NoInternet

        is SocketTimeoutException,
        is HttpRequestTimeoutException -> DataError.Network.RequestTimeout

        else -> {
            Log.e(TAG, "asNetworkError: ", this)
            DataError.Network.Unknown
        }
    }

    companion object {
        private const val TAG = "KtorClient"
    }
}
