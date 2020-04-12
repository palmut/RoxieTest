package net.palmut.roxietest

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import net.palmut.roxietest.net.Address
import net.palmut.roxietest.net.Order
import net.palmut.roxietest.net.Price
import net.palmut.roxietest.net.Vehicle
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ParserTest {

    @Test
    fun address_parser_test() {
        val data = """
            {
                "city": "Санкт-Петербург",
                "address": "Пр. Ленина, д. 1"
            }
        """.trimIndent()

        val json = Json(JsonConfiguration.Stable)
        val address = json.parse(Address.serializer(), data)
        Assert.assertNotNull(address)
        Assert.assertEquals("Санкт-Петербург", address.city)
        Assert.assertEquals("Пр. Ленина, д. 1", address.address)

    }

    @Test
    fun price_parser_test() {
        val data = """
            {
                "amount": 73100,
                "currency": "RUB"
            }
        """.trimIndent()

        val json = Json(JsonConfiguration.Stable)
        val price = json.parse(Price.serializer(), data)
        Assert.assertNotNull(price)
        Assert.assertEquals(73100, price.amount)
        Assert.assertEquals("RUB", price.currency)

    }

    @Test
    fun vehicle_parser_test() {
        val data = """
            {
                "regNumber": "к345тт25",
                "modelName": "Toyota Camry",
                "photo": "02.jpg",
                "driverName": "Петров Петр Петрович"
            }
        """.trimIndent()

        val json = Json(JsonConfiguration.Stable)
        val vehicle = json.parse(Vehicle.serializer(), data)
        Assert.assertNotNull(vehicle)
        Assert.assertEquals("к345тт25", vehicle.regNumber)
        Assert.assertEquals("Toyota Camry", vehicle.modelName)
        Assert.assertEquals("02.jpg", vehicle.photo)
        Assert.assertEquals("Петров Петр Петрович", vehicle.driverName)

    }

    @Test
    fun order_parser_test() {
        val data = """
            {
                "id": 4869,
                "startAddress": {
                    "city": "Санкт-Петербург",
                    "address": "Пр. Кантемировская, д. 28"
                },
                "endAddress": {
                    "city": "Санкт-Петербург",
                    "address": "Пр. Стечек, д. 50"
                },
                "price": {
                    "amount": 73100,
                    "currency": "RUB"
                },
                "orderTime": "2016-05-27T19:50:00+03:00",
                "vehicle": {
                    "regNumber": "к345тт25",
                    "modelName": "Toyota Camry",
                    "photo": "02.jpg",
                    "driverName": "Петров Петр Петрович"
                }
            }
        """.trimIndent()

        val json = Json(JsonConfiguration.Stable)
        val order = json.parse(Order.serializer(), data)
        Assert.assertNotNull(order)

    }

}