package com.lydone.restaurantmanagerapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
)