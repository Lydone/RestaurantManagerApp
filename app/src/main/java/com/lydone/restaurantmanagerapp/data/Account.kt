package com.lydone.restaurantmanagerapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Account(
    @SerialName("name") val name: String,
    @SerialName("login") val login: String,
    @SerialName("password") val password: String,
    @SerialName("role") val role: Role,
) {

    @Serializable
    enum class Role {
        @SerialName("WAITER")
        WAITER,

        @SerialName("COOK")
        COOK,

        @SerialName("MANAGER")
        MANAGER,
    }
}