package net.palmut.roxietest.net

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import net.palmut.roxietest.BuildConfig
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private const val DEFAULT_TIMEOUT: Long = 90
private const val ORDERS_API_URL = "https://www.roxiemobile.ru/careers/test/"

object NetworkManager {

    private val okHttpClient by lazy {
        OkHttpClient.Builder().apply {
            readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })
        }.build()
    }

    val ordersApi: OrdersAPI by lazy {
        Retrofit.Builder().apply {
            baseUrl(ORDERS_API_URL)
            client(okHttpClient)
            MediaType
            addConverterFactory(
                Json(
                    JsonConfiguration(
                        encodeDefaults = false, isLenient = true)).asConverterFactory("application/json".toMediaType()))
        }.build().create(OrdersAPI::class.java)
    }


}

