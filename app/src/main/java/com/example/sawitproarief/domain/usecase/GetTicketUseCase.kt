package com.example.sawitproarief.domain.usecase

import com.example.sawitproarief.domain.model.WeighbridgeTicket
import com.example.sawitproarief.domain.repository.WeighbridgeRepository
import com.example.sawitproarief.domain.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTicketUseCase @Inject constructor(
    private val repository: WeighbridgeRepository
): UseCase<GetTicketUseCase.Param, Flow<WeighbridgeTicket>>() {

    override suspend fun doWork(params: Param): Flow<WeighbridgeTicket> {
        return repository.getTicketFlow(params.ticketId)
    }

    data class Param(val ticketId: String)
}