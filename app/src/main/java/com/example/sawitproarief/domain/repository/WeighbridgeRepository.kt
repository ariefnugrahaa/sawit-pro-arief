package com.example.sawitproarief.domain.repository

import com.example.sawitproarief.domain.model.WeighbridgeTicket
import kotlinx.coroutines.flow.Flow

interface WeighbridgeRepository {
    suspend fun createTicket(ticket: WeighbridgeTicket)
    suspend fun updateTicket(ticket: WeighbridgeTicket)
    suspend fun getTicket(id: String): WeighbridgeTicket
    fun getAllTickets(): Flow<List<WeighbridgeTicket>>
    suspend fun deleteTicket(id: String)
    fun getTicketFlow(ticketId: String): Flow<WeighbridgeTicket>
}