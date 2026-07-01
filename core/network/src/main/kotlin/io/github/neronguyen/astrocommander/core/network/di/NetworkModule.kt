package io.github.neronguyen.astrocommander.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.neronguyen.astrocommander.core.network.AscomNetworkDataSource
import io.github.neronguyen.astrocommander.core.network.ktor.KtorAscomNetworkClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideAscomNetworkDataSource(httpClient: HttpClient): AscomNetworkDataSource {
        return KtorAscomNetworkClient(httpClient)
    }

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(OkHttp) {
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
        }

        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.BODY
        }
    }
}
