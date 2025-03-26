package com.example.sawitproarief.domain.usecase

import com.example.sawitproarief.domain.model.WeighbridgeTicket
import com.example.sawitproarief.domain.repository.WeighbridgeRepository
import com.example.sawitproarief.domain.usecase.base.NoParamsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllTicketsUseCase @Inject constructor(
    private val repository: WeighbridgeRepository
) : NoParamsUseCase<Flow<List<WeighbridgeTicket>>>() {
    override suspend fun doWork(): Flow<List<WeighbridgeTicket>> {
        return repository.getAllTickets()
    }
}