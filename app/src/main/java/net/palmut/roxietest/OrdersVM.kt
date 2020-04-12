package net.palmut.roxietest

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import net.palmut.roxietest.net.OrdersRepository
import net.palmut.roxietest.net.OrdersRepositoryInterface
import net.palmut.roxietest.net.RepositoryResult

class OrdersVM(private val repository: OrdersRepositoryInterface) : ViewModel() {

    val orders = flow {
        emit(ViewModelResult.Progress)
        when (val result = repository.getOrders()) {
            is RepositoryResult.Success -> emit(ViewModelResult.Success(result.data.sortedByDescending { it.orderTime }))
            is RepositoryResult.Error -> emit(ViewModelResult.Error(result.e))
        }
    }.flowOn(Dispatchers.IO).asLiveData(Dispatchers.Main)

    fun photo(name: String) = flow<ViewModelResult<Bitmap>> {
        emit(ViewModelResult.Progress)
        when (val result = repository.getImage(name)) {
            is RepositoryResult.Success -> {
                try {
                    emit(ViewModelResult.Success(BitmapFactory.decodeStream(result.data)))
                } catch (e: Exception) {
                    emit(ViewModelResult.Error(e))
                }
            }
            is RepositoryResult.Error -> emit(ViewModelResult.Error(result.e))
        }
    }.flowOn(Dispatchers.IO).asLiveData(Dispatchers.Main)
}

sealed class ViewModelResult<out T> {
    object Progress : ViewModelResult<Nothing>()
    data class Success<T>(val data: T) : ViewModelResult<T>()
    data class Error(val exception: Exception) : ViewModelResult<Nothing>()
}

@Suppress("UNCHECKED_CAST")
class OrderVMFactory(private val repository: OrdersRepositoryInterface): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OrdersVM(repository) as T
    }
}