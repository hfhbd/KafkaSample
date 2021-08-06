import kotlinx.serialization.*
import java.util.*


val inputTopic = object : Topic<Data> {
    override val name = "input-data"
    override val serializer= Data.serializer()
}

val outputTopic = object : Topic<Classified> {
    override val name = "output-data"
    override val serializer = Classified.serializer()
}

interface Topic<T> {
    val name: String
    val serializer: KSerializer<T>
}

fun kafkaProperties(port: Int) = Properties().apply {
    this["bootstrap.servers"] = "localhost:$port"
    this["acks"] = "all"
    this["retries"] = 0
    this["batch.size"] = 16384
    this["group.id"] = "test"
    this["linger.ms"] = 0
    this["buffer.memory"] = 33554432
    this["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
    this["value.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
    this["key.deserializer"] = "org.apache.kafka.common.serialization.StringDeserializer"
    this["value.deserializer"] = "org.apache.kafka.common.serialization.StringDeserializer"
}
