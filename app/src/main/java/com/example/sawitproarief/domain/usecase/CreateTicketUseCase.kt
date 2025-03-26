package com.example.sawitproarief.domain.usecase

import com.example.sawitproarief.domain.model.WeighbridgeTicket
import com.example.sawitproarief.domain.repository.WeighbridgeRepository
import com.example.sawitproarief.domain.usecase.base.UseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateTicketUseCase @Inject constructor(
    private val repository: WeighbridgeRepository
) : UseCase<CreateTicketUseCase.Param, Unit>() {
    override suspend fun doWork(params: Param) {
        repository.createTicket(params.ticket)
    }

    data class Param(
        val ticket: WeighbridgeTicket
    )
}