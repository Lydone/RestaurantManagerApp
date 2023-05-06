package com.lydone.restaurantmanagerapp.data

import javax.inject.Inject

class AccountRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getAccounts() = apiService.getAccounts()

    suspend fun deleteAccount(login: String) = apiService.deleteAccount(login)

    suspend fun addAccount(account: Account) = apiService.addAccount(account)
}