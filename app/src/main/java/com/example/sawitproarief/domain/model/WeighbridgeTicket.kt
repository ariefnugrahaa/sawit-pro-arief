package com.example.sawitproarief.domain.model

import com.example.sawitproarief.data.local.entity.WeighbridgeTicketEntity
import java.util.UUID

data class WeighbridgeTicket(
    val id: String = UUID.randomUUID().toString(),
    val dateTime: Long = System.currentTimeMillis(),
    val licenseNumber: String = "",
    val driverName: String = "",
    val inboundWeight: Double = 0.0,
    val outboundWeight: Double = 0.0,
    val netWeight: Double = 0.0
)

fun WeighbridgeTicket.toEntity() = WeighbridgeTicketEntity(
    id = id,
    dateTime = dateTime,
    licenseNumber = licenseNumber,
    driverName = driverName,
    inboundWeight = inboundWeight,
    outboundWeight = outboundWeight,
    netWeight = netWeight
)