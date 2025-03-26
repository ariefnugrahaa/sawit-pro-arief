package com.example.sawitproarief.domain.usecase

import com.example.sawitproarief.domain.repository.WeighbridgeRepository
import com.example.sawitproarief.domain.usecase.base.UseCase
import javax.inject.Inject

class DeleteTicketUseCase @Inject constructor(
    private val repository: WeighbridgeRepository,
): UseCase<DeleteTicketUseCase.Param, Unit>() {

    override suspend fun doWork(params: Param) {
        repository.deleteTicket(params.id)
    }

    data class Param(
        val id: String
    )
}