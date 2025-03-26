package com.example.sawitproarief.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.sawitproarief.data.repository.WeighbridgeRepositoryImpl
import com.example.sawitproarief.domain.repository.WeighbridgeRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: WeighbridgeRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            (repository as WeighbridgeRepositoryImpl).syncPendingTickets()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}