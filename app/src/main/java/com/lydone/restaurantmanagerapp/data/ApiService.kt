package com.lydone.restaurantmanagerapp.data

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("dish")
    suspend fun getDishes(): List<Dish>

    @POST("dish")
    suspend fun addDish(@Body request: AddDishRequest)

    @DELETE("dish/{id}")
    suspend fun deleteDish(@Path("id") id: Int)

    @POST("stop_list/{dish}")
    suspend fun addDishToStopList(@Path("dish") id: Int)

    @DELETE("stop_list/{dish}")
    suspend fun removeDishFromStopList(@Path("dish") id: Int)

    @GET("category")
    suspend fun getCategories(): List<Category>

    @POST("category/{name}")
    suspend fun addCategory(@Path("name") name: String)

    @DELETE("category/{id}")
    suspend fun deleteCategory(@Path("id") id: Int)

    @GET("account")
    suspend fun getAccounts(): List<Account>

    @POST("account")
    suspend fun addAccount(@Body account: Account)

    @DELETE("account/{login}")
    suspend fun deleteAccount(@Path("login") login: String)

    @GET("table")
    suspend fun getTables(): List<Table>

    @DELETE("table/{number}")
    suspend fun deleteTable(@Path("number") number: Int)

    @POST("table/{number}")
    suspend fun addTable(@Path("number") number: Int)

    @GET("order/all_table/{table}")
    suspend fun getOrders(@Path("table") table: Int): List<Order>

    @DELETE("order/entry/{id}")
    suspend fun deleteOrderEntry(@Path("id") id: Int)
}