package com.example.sawitproarief.utils

fun isValidLicenseNumber(number: String): Boolean {
    val regex = "^[A-Z]{1,2}\\d{1,4}[A-Z]{1,3}$".toRegex()
    return number.matches(regex)
}

fun validateInboundWeight(weight: String): String? {
    if (weight.isBlank()) return "Berat inbound harus diisi"
    val weightValue = weight.toDoubleOrNull()
    return when {
        weightValue == null -> "Berat harus berupa angka"
        weightValue <= 0 -> "Berat inbound harus lebih dari 0"
        else -> null
    }
}

fun validateOutboundWeight(outbound: String, inbound: String): String? {
    if (inbound.isBlank()) return "Berat inbound harus diisi terlebih dahulu"
    if (outbound.isBlank()) return "Berat outbound harus diisi"

    val outboundValue = outbound.toDoubleOrNull()
    return when {
        outboundValue == null -> "Berat harus berupa angka"
        outboundValue <= 0 -> "Berat outbound harus lebih dari 0"
        else -> null
    }
}