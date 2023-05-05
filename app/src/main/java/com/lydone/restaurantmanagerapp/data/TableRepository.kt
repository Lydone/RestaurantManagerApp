package com.lydone.restaurantmanagerapp.data

import javax.inject.Inject

class TableRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun addTable(number: Int) = apiService.addTable(number)

    suspend fun getTables() = apiService.getTables()

    suspend fun deleteTable(number: Int) = apiService.deleteTable(number)
}