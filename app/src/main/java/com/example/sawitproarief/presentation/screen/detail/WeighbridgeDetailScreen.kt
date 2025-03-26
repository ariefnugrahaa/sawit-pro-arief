package com.example.sawitproarief.presentation.screen.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sawitproarief.R
import com.example.sawitproarief.domain.model.WeighbridgeTicket
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeighbridgeDetailScreen(
    ticket: WeighbridgeTicket,
    onNavigateBack: () -> Unit,
    onEditClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_ticket_detail)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onEditClick(ticket.id) }) {
                        Icon(Icons.Default.Edit, "Edit")
                    }
                    IconButton(onClick = { onDeleteClick(ticket.id) }) {
                        Icon(Icons.Default.Delete, "Delete")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            DetailItem(
                label = stringResource(R.string.detail_date),
                value = stringResource(R.string.detail_date_value, DateFormat.getDateTimeInstance().format(Date(ticket.dateTime)))
            )
            Spacer(modifier = Modifier.height(16.dp))
            DetailItem(
                label = stringResource(R.string.detail_license),
                value = stringResource(R.string.detail_license_value, ticket.licenseNumber)
            )
            Spacer(modifier = Modifier.height(16.dp))
            DetailItem(
                label = stringResource(R.string.detail_driver),
                value = stringResource(R.string.detail_driver_value, ticket.driverName)
            )
            Spacer(modifier = Modifier.height(16.dp))
            DetailItem(
                label = stringResource(R.string.detail_inbound),
                value = stringResource(R.string.detail_inbound_value, ticket.inboundWeight.toString())
            )
            Spacer(modifier = Modifier.height(16.dp))
            DetailItem(
                label = stringResource(R.string.detail_outbound),
                value = stringResource(R.string.detail_outbound_value, ticket.outboundWeight.toString())
            )
            Spacer(modifier = Modifier.height(16.dp))
            DetailItem(
                label = stringResource(R.string.detail_net),
                value = stringResource(R.string.detail_net_value, ticket.netWeight.toString()),
                isHighlighted = true
            )
        }
    }
}

@Composable
private fun DetailItem(
    label: String,
    value: String,
    isHighlighted: Boolean = false
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isHighlighted) MaterialTheme.colorScheme.primary 
                   else MaterialTheme.colorScheme.onSurface
        )
    }
}