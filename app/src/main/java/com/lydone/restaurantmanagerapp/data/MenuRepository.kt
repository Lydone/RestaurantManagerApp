package com.lydone.restaurantmanagerapp.data

import javax.inject.Inject

class MenuRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getDishes() = apiService.getDishes()
    suspend fun addToStopList(id: Int) = apiService.addDishToStopList(id)
    suspend fun removeFromStopList(id: Int) = apiService.removeDishFromStopList(id)
    suspend fun addDish(addDishRequest: AddDishRequest) = apiService.addDish(addDishRequest)
    suspend fun deleteDish(id: Int) = apiService.deleteDish(id)
    suspend fun getCategories() = apiService.getCategories()
    suspend fun addCategory(name: String) = apiService.addCategory(name)
    suspend fun deleteCategory(id: Int) = apiService.deleteCategory(id)
}