package net.palmut.roxietest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import net.palmut.roxietest.net.OrdersRepository
import net.palmut.roxietest.net.OrdersRepositoryInterface
import net.palmut.roxietest.net.RepositoryResult

class OrdersVM(private val repository: OrdersRepositoryInterface = OrdersRepository) : ViewModel() {

    val orders = flow {
        emit(ViewModelResult.Progress)
        when(val result = repository.getOrders()) {
            is RepositoryResult.Success -> emit(ViewModelResult.Success(result.data.sortedByDescending { it.orderTime }))
            is RepositoryResult.Error -> emit(ViewModelResult.Error(result.e))
        }
    }.flowOn(Dispatchers.IO).asLiveData(Dispatchers.Main)

}

sealed class ViewModelResult<out T> {
    object Progress : ViewModelResult<Nothing>()
    data class Success<T>(val data: T) : ViewModelResult<T>()
    data class Error(val exception: Exception) : ViewModelResult<Nothing>()
}