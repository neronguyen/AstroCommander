package io.github.neronguyen.astrocommander

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.BackoffPolicy
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import coil3.ImageLoader
import coil3.SingletonImageLoader
import dagger.hilt.android.HiltAndroidApp
import io.github.neronguyen.astrocommander.worker.RandomIdWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class AscomApplication : Application(), Configuration.Provider, SingletonImageLoader.Factory {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var imageLoader: ImageLoader

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        scheduleRandomIdWork()
    }

    override fun newImageLoader(context: Context): ImageLoader {
        return imageLoader
    }

    fun scheduleRandomIdWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Only for testing. Switch to PeriodicWorkRequestBuilder for production.
        val workRequest = OneTimeWorkRequestBuilder<RandomIdWorker>()
            .setInitialDelay(15, TimeUnit.SECONDS)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            "RandomIdWork",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}
