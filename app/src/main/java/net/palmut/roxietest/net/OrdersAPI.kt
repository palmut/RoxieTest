package net.palmut.roxietest.net

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface OrdersAPI {

    @GET("orders.json")
    suspend fun getOrders(): List<Order>

    @GET("images/{imageName}")
    suspend fun getImage(@Path("imageName") imageName: String): ResponseBody
}