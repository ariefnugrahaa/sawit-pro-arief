package com.example.sawitproarief.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sawitproarief.R
import com.example.sawitproarief.domain.model.WeighbridgeTicket
import com.example.sawitproarief.utils.Constants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WeighbridgeCard(
    ticket: WeighbridgeTicket,
    onTicketClick: (String) -> Unit,
    onEditClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onTicketClick(ticket.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = ticket.licenseNumber,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = SimpleDateFormat(
                            Constants.FULL_DATE_TIME_FORMAT,
                            Locale.getDefault()
                        ).format(Date(ticket.dateTime)),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                IconButton(onClick = { onEditClick(ticket.id) }) {
                    Icon(Icons.Default.Edit, "Filter")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.detail_driver_value, ticket.driverName),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(
                            R.string.detail_inbound_value,
                            ticket.inboundWeight.toString()
                        ),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = stringResource(
                            R.string.detail_outbound_value,
                            ticket.outboundWeight.toString()
                        ),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(
                    text = stringResource(R.string.detail_net_value, ticket.netWeight.toString()),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}