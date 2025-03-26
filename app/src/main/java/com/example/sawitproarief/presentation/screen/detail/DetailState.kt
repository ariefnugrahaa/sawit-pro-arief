package com.example.sawitproarief.presentation.screen.detail

import androidx.compose.runtime.Immutable
import com.example.sawitproarief.domain.model.WeighbridgeTicket

@Immutable
data class WeighbridgeDetailUIState(
    val ticket: WeighbridgeTicket?,
    val error: String?
)