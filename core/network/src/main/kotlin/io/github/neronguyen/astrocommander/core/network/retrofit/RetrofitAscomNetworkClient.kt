package io.github.neronguyen.astrocommander.core.network.retrofit

import android.util.Log
import io.github.neronguyen.astrocommander.core.network.AscomNetworkDataSource
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET

private interface RetrofitAscomNetworkApi {

    @GET("todos/1")
    suspend fun getPlaceholderJson(): JsonElement
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

    init {
        Log.d("TAGTAG", "provideOkHttpClient")
    }

    override suspend fun getPlaceholderJson(): String {
        return networkClient.getPlaceholderJson().toString()
    }
}
