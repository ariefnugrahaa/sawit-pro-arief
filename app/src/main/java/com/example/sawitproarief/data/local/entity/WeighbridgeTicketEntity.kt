package com.example.sawitproarief.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sawitproarief.domain.model.WeighbridgeTicket

@Entity(tableName = "weighbridge_tickets")
data class WeighbridgeTicketEntity(
    @PrimaryKey
    val id: String,
    val dateTime: Long,
    val licenseNumber: String,
    val driverName: String,
    val inboundWeight: Double,
    val outboundWeight: Double,
    val netWeight: Double,
    val syncStatus: SyncStatus = SyncStatus.PENDING
)

fun WeighbridgeTicketEntity.toDomain() = WeighbridgeTicket(
    id = id,
    dateTime = dateTime,
    licenseNumber = licenseNumber,
    driverName = driverName,
    inboundWeight = inboundWeight,
    outboundWeight = outboundWeight,
    netWeight = netWeight
)

enum class SyncStatus(val status: String) {
    SYNCED("SYNCED"),
    PENDING("PENDING"),
    FAILED("FAILED"),
}