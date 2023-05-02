package com.lydone.restaurantmanagerapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddDishRequest(
    @SerialName("imageBase64") val imageBase64: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("price") val price: Int,
    @SerialName("weight") val weight: Int,
    @SerialName("categoryId") val categoryId: Int,
)