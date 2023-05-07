package com.lydone.restaurantmanagerapp.data

import javax.inject.Inject

class OrderRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getOrders(table: Int) = apiService.getOrders(table).sortedByDescending(Order::id)

    suspend fun deleteOrderEntry(id: Int) = apiService.deleteOrderEntry(id)
}