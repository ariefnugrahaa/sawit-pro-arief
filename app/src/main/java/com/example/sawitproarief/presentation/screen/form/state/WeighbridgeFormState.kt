package com.example.sawitproarief.presentation.screen.form.state

import androidx.compose.runtime.Immutable
import com.example.sawitproarief.domain.model.WeighbridgeTicket

@Immutable
data class WeighbridgeFormState(
    val currentTicket: WeighbridgeTicket? = null,
    val error: String? = null,
)