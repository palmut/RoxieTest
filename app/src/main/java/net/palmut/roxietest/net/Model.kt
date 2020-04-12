package net.palmut.roxietest.net

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.text.SimpleDateFormat
import java.util.*

@Serializable
data class Order(val id: Int,
                 val startAddress: Address,
                 val endAddress: Address,
                 val price: Price,
                 @Serializable(with = DateSerializer::class) val orderTime: Date,
                 val vehicle: Vehicle)

@Serializable
data class Address(val city: String, val address: String)

@Serializable
data class Price(val amount: Int, val currency: String)

@Serializable
data class Vehicle(val regNumber: String, val modelName: String, val photo: String, val driverName: String)

@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date> {
    private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
    override val descriptor: SerialDescriptor = SerialDescriptor("DateSerializer")

    override fun serialize(output: Encoder, obj: Date) {
        output.encodeString(formatter.format(obj))
    }

    override fun deserialize(input: Decoder): Date {
        return formatter.parse(input.decodeString())
    }
}

fun Order.toExtra() = Json(JsonConfiguration.Stable).stringify(Order.serializer(),this)
fun String.toOrder() = Json(JsonConfiguration.Stable).parse(Order.serializer(), this)