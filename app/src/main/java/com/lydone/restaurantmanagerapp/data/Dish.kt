package com.lydone.restaurantmanagerapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Dish(
    @SerialName("id") val id: Int,
    @SerialName("url") val url: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("price") val price: Int,
    @SerialName("weight") val weight: Int,
    @SerialName("category") val category: Category,
    @SerialName("inStopList") val inStopList: Boolean,
)