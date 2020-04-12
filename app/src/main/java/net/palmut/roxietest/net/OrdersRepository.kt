package net.palmut.roxietest.net

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.InputStream
import java.net.HttpURLConnection

interface OrdersRepositoryInterface {
    suspend fun getOrders(): RepositoryResult<List<Order>>
    suspend fun getImage(imageName: String): RepositoryResult<InputStream>
}

object OrdersRepository : OrdersRepositoryInterface {
    override suspend fun getOrders(): RepositoryResult<List<Order>> = tryRequest { NetworkManager.ordersApi.getOrders() }
    override suspend fun getImage(imageName: String): RepositoryResult<InputStream> = tryRequest {
        NetworkManager.ordersApi.getImage(imageName).byteStream()
    }
}

sealed class RepositoryResult<out T> {
    data class Success<T>(val data: T) : RepositoryResult<T>()
    data class Error(val e: Exception) : RepositoryResult<Nothing>()
}

suspend inline fun <T : Any> tryRequest(crossinline block: suspend () -> T): RepositoryResult<T> = withContext(Dispatchers.IO) {
    try {
        RepositoryResult.Success(block())
    } catch (e: HttpException) {
        when (e.code()) {
            HttpURLConnection.HTTP_BAD_REQUEST -> {
                RepositoryResult.Error(e)
            }
            else -> RepositoryResult.Error(e)
        }
    } catch (e: Exception) {
        RepositoryResult.Error(e)
    }
}