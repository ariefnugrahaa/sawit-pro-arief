package com.example.sawitproarief.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.sawitproarief.data.local.entity.WeighbridgeTicketEntity
import com.example.sawitproarief.domain.model.WeighbridgeTicket
import kotlinx.coroutines.flow.Flow

@Dao
interface WeighbridgeDao {
    @Query("SELECT * FROM weighbridge_tickets WHERE id = :id")
    suspend fun getTicketById(id: String): WeighbridgeTicketEntity?

    @Query("SELECT * FROM weighbridge_tickets WHERE syncStatus = 'PENDING'")
    suspend fun getPendingTickets(): List<WeighbridgeTicketEntity>

    @Query("SELECT * FROM weighbridge_tickets")
    fun getAllTickets(): Flow<List<WeighbridgeTicketEntity>>

    @Upsert
    suspend fun upsertTicket(ticket: WeighbridgeTicketEntity)

    @Upsert
    suspend fun upsertTickets(tickets: List<WeighbridgeTicketEntity>)

    @Query("SELECT id FROM weighbridge_tickets")
    suspend fun getAllTicketIds(): List<String>

    @Query("DELETE FROM weighbridge_tickets WHERE id = :ticketId")
    suspend fun deleteTicketById(ticketId: String)

    @Query("SELECT * FROM weighbridge_tickets WHERE id = :ticketId")
    fun getTicketFlow(ticketId: String): Flow<WeighbridgeTicket>
}