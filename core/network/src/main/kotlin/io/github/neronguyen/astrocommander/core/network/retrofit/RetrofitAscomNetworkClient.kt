package io.github.neronguyen.astrocommander.core.network.retrofit

import android.util.Log
import arrow.core.raise.Raise
import io.github.neronguyen.astrocommander.core.model.error.DataError
import io.github.neronguyen.astrocommander.core.network.AscomNetworkDataSource
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.channels.UnresolvedAddressException

private interface RetrofitAscomNetworkApi {

    @GET("todos/1")
    suspend fun getPlaceholderJson(): Response<JsonElement>
}

internal class RetrofitAscomNetworkClient(
    okHttpClient: OkHttpClient,
    networkJson: Json
) : AscomNetworkDataSource {

    private val networkClient = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com")
        .client(okHttpClient)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitAscomNetworkApi::class.java)


    context(raise: Raise<DataError.Network>)
    override suspend fun getPlaceholderJson(): String {
        return safeCall {
            networkClient.getPlaceholderJson()
        }.toString()
    }

    context(raise: Raise<DataError.Network>)
    private suspend inline fun <reified T> safeCall(
        execute: suspend () -> Response<T>
    ): T {
        try {
            val response = execute()
            if (!response.isSuccessful) {
                raise.raise(DataError.Network.Unknown)
            }

            return response.body()
                ?: raise.raise(DataError.Network.Unknown)

        } catch (_: SerializationException) {
            raise.raise(DataError.Network.Serialization)
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            Log.e(TAG, "safeCall: ", e)
            raise.raise(e.asNetworkError())
        }
    }

    private fun Exception.asNetworkError(): DataError.Network = when (this) {
        is UnknownHostException,
        is UnresolvedAddressException,
        is ConnectException -> DataError.Network.NoInternet

        is SocketTimeoutException -> DataError.Network.RequestTimeout

        else -> {
            Log.e(TAG, "asNetworkError: ", this)
            DataError.Network.Unknown
        }
    }

    companion object {
        private const val TAG = "RetrofitAscomNetworkClient"
    }
}
