package com.example.sawitproarief.presentation.screen.ticket.state

import androidx.compose.runtime.Immutable
import com.example.sawitproarief.domain.model.WeighbridgeTicket

@Immutable
data class WeighbridgeUiState(
    val tickets: List<WeighbridgeTicket> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val sortOrder: SortOrder = SortOrder.DATE_DESC,
    val filterQuery: String = "",
    val startDate: Long? = null,
    val endDate: Long? = null,
)

enum class SortOrder {
    DATE_ASC, DATE_DESC, DRIVER_NAME, LICENSE_NUMBER
}