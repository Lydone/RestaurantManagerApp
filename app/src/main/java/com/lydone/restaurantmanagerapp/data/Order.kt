package com.lydone.restaurantmanagerapp.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    @SerialName("id") val id: Int,
    @SerialName("table") val table: Int,
    @SerialName("entries") val entries: List<Entry>,
    @SerialName("closeInstant") val closeInstant: Instant?,
) {

    @Serializable
    data class Entry(
        @SerialName("id") val id: Int,
        @SerialName("dish") val dish: Dish,
        @SerialName("status") val status: Status,
        @SerialName("instant") val instant: Instant,
        @SerialName("comment") val comment: String?,
        @SerialName("table") val table: Int,
    ) {

        @Serializable
        enum class Status {
            @SerialName("QUEUE")
            QUEUE,

            @SerialName("COOKING")
            COOKING,

            @SerialName("READY")
            READY
        }
    }
}