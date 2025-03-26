package com.example.sawitproarief.data.repository

import com.example.sawitproarief.data.local.WeighbridgeDao
import com.example.sawitproarief.data.local.entity.SyncStatus
import com.example.sawitproarief.data.local.entity.toDomain
import com.example.sawitproarief.data.remote.FirebaseWeighbridgeService
import com.example.sawitproarief.domain.model.WeighbridgeTicket
import com.example.sawitproarief.domain.model.toEntity
import com.example.sawitproarief.domain.repository.WeighbridgeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WeighbridgeRepositoryImpl(
    private val localDataSource: WeighbridgeDao,
    private val remoteDataSource: FirebaseWeighbridgeService
) : WeighbridgeRepository {

    override suspend fun createTicket(ticket: WeighbridgeTicket) {
        val entity = ticket.toEntity().copy(syncStatus = SyncStatus.PENDING)
        localDataSource.upsertTicket(entity)
        try {
            remoteDataSource.createTicket(ticket)
            localDataSource.upsertTicket(entity.copy(syncStatus = SyncStatus.SYNCED))
        } catch (e: Exception) {
            localDataSource.upsertTicket(entity.copy(syncStatus = SyncStatus.FAILED))
            throw e
        }
    }

    override suspend fun updateTicket(ticket: WeighbridgeTicket) {
        val entity = ticket.toEntity().copy(syncStatus = SyncStatus.PENDING)
        localDataSource.upsertTicket(entity)
        try {
            remoteDataSource.updateTicket(ticket)
            localDataSource.upsertTicket(entity.copy(syncStatus = SyncStatus.SYNCED))
        } catch (e: Exception) {
            localDataSource.upsertTicket(entity.copy(syncStatus = SyncStatus.FAILED))
            throw e
        }
    }

    override suspend fun deleteTicket(id: String) {
        try {
            localDataSource.deleteTicketById(id)
            remoteDataSource.deleteTicket(id)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getTicketFlow(ticketId: String): Flow<WeighbridgeTicket> {
        return localDataSource.getTicketFlow(ticketId)
    }

    suspend fun syncPendingTickets() {
        localDataSource.getPendingTickets().forEach { entity ->
            try {
                remoteDataSource.createTicket(entity.toDomain())
                localDataSource.upsertTicket(entity.copy(syncStatus = SyncStatus.SYNCED))
            } catch (e: Exception) {
                localDataSource.upsertTicket(entity.copy(syncStatus = SyncStatus.FAILED))
            }
        }
    }

    override suspend fun getTicket(id: String): WeighbridgeTicket {
        return localDataSource.getTicketById(id)?.toDomain()
            ?: throw IllegalStateException("Ticket not found")
    }

    override fun getAllTickets(): Flow<List<WeighbridgeTicket>> {
        return localDataSource.getAllTickets()
            .map { entities -> 
                entities.map { entity -> entity.toDomain() } 
            }
    }
}