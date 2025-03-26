package com.example.sawitproarief.presentation.screen.form

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sawitproarief.R
import com.example.sawitproarief.domain.model.WeighbridgeTicket
import com.example.sawitproarief.presentation.screen.form.viewmodel.WeighbridgeFormViewModel
import com.example.sawitproarief.utils.Constants.FULL_DATE_TIME_FORMAT
import com.example.sawitproarief.utils.isValidLicenseNumber
import com.example.sawitproarief.utils.validateInboundWeight
import com.example.sawitproarief.utils.validateOutboundWeight
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

@SuppressLint("DefaultLocale")
@Composable
fun WeighbridgeFormScreen(
    ticketId: String,
    viewModel: WeighbridgeFormViewModel = hiltViewModel(),
    onSubmit: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    var selectedDate by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val dateFormatter = remember { SimpleDateFormat(FULL_DATE_TIME_FORMAT, Locale.getDefault()) }
    
    val timePickerDialog = remember {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = selectedDate
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }
                selectedDate = calendar.timeInMillis
            },
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
            Calendar.getInstance().get(Calendar.MINUTE),
            true
        )
    }

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                selectedDate = calendar.timeInMillis
                timePickerDialog.show()
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
    }

    LaunchedEffect(ticketId) {
        viewModel.loadTicket(ticketId)
    }

    var licenseNumber by remember { mutableStateOf("") }
    var licenseNumberError by remember { mutableStateOf<String?>(null) }
    var driverName by remember { mutableStateOf("") }
    var driverNameError by remember { mutableStateOf<String?>(null) }
    var inboundWeight by remember { mutableStateOf("") }
    var inboundWeightError by remember { mutableStateOf<String?>(null) }
    var outboundWeight by remember { mutableStateOf("") }
    var outboundWeightError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiState) {
        uiState.let { state ->
            state.currentTicket?.let { ticket ->
                licenseNumber = ticket.licenseNumber
                driverName = ticket.driverName
                inboundWeight = ticket.inboundWeight.toString()
                outboundWeight = ticket.outboundWeight.toString()
            }
        }
    }

    val netWeight = remember(inboundWeight, outboundWeight) {
        val inbound = inboundWeight.toDoubleOrNull() ?: 0.0
        val outbound = outboundWeight.toDoubleOrNull() ?: 0.0
        outbound - inbound
    }

    var netWeightError by remember { mutableStateOf<String?>(null) }

    val isFormValid = remember(
        licenseNumberError, driverNameError, 
        inboundWeightError, outboundWeightError,
        licenseNumber, driverName, 
        inboundWeight, outboundWeight,
        netWeight
    ) {
        licenseNumberError == null && 
        driverNameError == null &&
        inboundWeightError == null && 
        outboundWeightError == null &&
        licenseNumber.isNotBlank() &&
        driverName.isNotBlank() &&
        inboundWeight.isNotBlank() &&
        outboundWeight.isNotBlank() &&
        netWeight >= 0
    }

    LaunchedEffect(netWeight) {
        netWeightError = when {
            netWeight < 0 -> context.getString(R.string.error_weight_cannot_be_negative)
            netWeight == 0.0 -> context.getString(R.string.error_weight_cannot_be_zero)
            else -> null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextButton(
            onClick = { datePickerDialog.show() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                stringResource(
                    R.string.selected_date_time,
                    dateFormatter.format(Date(selectedDate))
                ))
        }

        OutlinedTextField(
            value = licenseNumber,
            onValueChange = {
                licenseNumber = it.uppercase()
                licenseNumberError =
                    if (!isValidLicenseNumber(it.uppercase())) context.getString(R.string.error_license_not_valid) else null
            },
            label = { Text(stringResource(R.string.label_license_number)) },
            isError = licenseNumberError != null,
            supportingText = { licenseNumberError?.let { Text(it) } },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )

        OutlinedTextField(
            value = driverName,
            onValueChange = {
                driverName = it
                driverNameError = if (it.isBlank()) context.getString(R.string.error_driver_name_required) else null
            },
            label = { Text(stringResource(R.string.label_driver_name)) },
            isError = driverNameError != null,
            supportingText = { driverNameError?.let { Text(it) } },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )

        OutlinedTextField(
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            value = inboundWeight,
            onValueChange = {
                inboundWeight = it
                inboundWeightError = validateInboundWeight(it)
            },
            label = { Text(stringResource(R.string.label_inbound_weight)) },
            isError = inboundWeightError != null,
            supportingText = { inboundWeightError?.let { Text(it) } },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )

        OutlinedTextField(
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            value = outboundWeight,
            onValueChange = {
                outboundWeight = it
                outboundWeightError = validateOutboundWeight(it, inboundWeight)
            },
            label = { Text(stringResource(R.string.label_outbound_weight)) },
            isError = outboundWeightError != null,
            supportingText = { outboundWeightError?.let { Text(it) } },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        netWeightError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Button(
            onClick = {
                val ticket = WeighbridgeTicket(
                    id = ticketId.ifEmpty { UUID.randomUUID().toString() },
                    dateTime = selectedDate,
                    licenseNumber = licenseNumber,
                    driverName = driverName,
                    inboundWeight = inboundWeight.toDoubleOrNull() ?: 0.0,
                    outboundWeight = outboundWeight.toDoubleOrNull() ?: 0.0,
                    netWeight = netWeight
                )

                if (ticketId.isEmpty()) {
                    viewModel.createTicket(ticket)
                } else {
                    viewModel.updateTicket(ticket)
                }
                onSubmit()
            },
            enabled = isFormValid && netWeight > 0,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                if (ticketId.isEmpty()) 
                    stringResource(R.string.button_create) 
                else 
                    stringResource(R.string.button_update)
            )
        }
    }
}