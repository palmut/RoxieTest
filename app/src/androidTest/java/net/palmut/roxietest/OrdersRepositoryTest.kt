package net.palmut.roxietest

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import net.palmut.roxietest.net.OrdersRepository
import net.palmut.roxietest.net.OrdersRepositoryInterface
import net.palmut.roxietest.net.RepositoryResult
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.Before
import org.junit.runner.RunWith
import retrofit2.HttpException
import java.util.*

@RunWith(AndroidJUnit4::class)
class OrdersRepositoryTest {

    private lateinit var repository: OrdersRepositoryInterface

    @Before
    fun setup() {
        repository = OrdersRepository
    }

    @After
    fun tearDown() {

    }

    @Test
    fun get_orders_test() {
        runBlocking {
            val result = repository.getOrders()
            Assert.assertTrue(result is RepositoryResult.Success)
            val list = (result as RepositoryResult.Success).data
            Assert.assertFalse(list.isEmpty())
        }
    }

    @Test
    fun get_image_test() {
        runBlocking {
            val photo = (repository.getOrders() as RepositoryResult.Success).data.first().vehicle.photo
            val result = repository.getImage(photo)
            Assert.assertTrue(result is RepositoryResult.Success)
            val stream = (result as RepositoryResult.Success).data
            val bitmap = BitmapFactory.decodeStream(stream)
            Assert.assertNotNull(bitmap)
        }
    }

    @Test
    fun get_random_image_test() {
        runBlocking {
            val photo = UUID.randomUUID().toString()
            val result = repository.getImage(photo)
            Assert.assertTrue(result is RepositoryResult.Error)
            val e = (result as RepositoryResult.Error).e
            Assert.assertTrue(e is HttpException)
        }
    }
}