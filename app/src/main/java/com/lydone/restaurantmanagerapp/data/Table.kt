package com.lydone.restaurantmanagerapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Table(
    @SerialName("number") val number: Int,
    @SerialName("qrCodeUrl") val qrCodeUrl: String,
)
