package com.example.sawitproarief.domain.usecase

import com.example.sawitproarief.domain.model.WeighbridgeTicket
import com.example.sawitproarief.domain.repository.WeighbridgeRepository
import com.example.sawitproarief.domain.usecase.base.UseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateTicketUseCase @Inject constructor(
    private val repository: WeighbridgeRepository
) : UseCase<UpdateTicketUseCase.Param, Unit>() {
    override suspend fun doWork(params: Param) {
        repository.updateTicket(params.ticket)
    }

    data class Param(
        val ticket: WeighbridgeTicket
    )
}