package io.github.neronguyen.astrocommander.worker

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import arrow.core.raise.context.either
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.github.neronguyen.astrocommander.AscomApplication
import io.github.neronguyen.astrocommander.R
import io.github.neronguyen.astrocommander.core.database.dao.PlaceholderJsonDao
import io.github.neronguyen.astrocommander.core.database.model.PlaceholderJsonEntity
import io.github.neronguyen.astrocommander.core.network.AscomNetworkDataSource

@HiltWorker
class RandomIdWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val placeholderJsonDao: PlaceholderJsonDao,
    private val networkDataSource: AscomNetworkDataSource,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        either {
            val randomId = (1..200).random()
            networkDataSource.getPlaceholderJson(randomId)
        }.onLeft {
            return Result.retry()
        }.onRight { result ->
            val entity = PlaceholderJsonEntity(
                id = result.id,
                title = "WorkManager ${result.title}",
                completed = result.completed
            )
            placeholderJsonDao.upsertPlaceholderJson(entity)
            showNotification(result.id.toInt(), result.title)
        }
        return Result.retry()
    }

    private fun showNotification(id: Int, title: String) {
        val builder = NotificationCompat.Builder(context, AscomApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Use a better icon if available
            .setContentTitle("New Data Fetched")
            .setContentText(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (androidx.core.app.ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                notify(id, builder.build())
            }
        }
    }
}
