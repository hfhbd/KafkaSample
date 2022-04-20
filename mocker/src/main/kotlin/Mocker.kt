import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import org.apache.kafka.clients.producer.*
import java.util.*
import kotlin.random.Random
import kotlin.time.*
import kotlin.time.Duration.Companion.seconds

@ExperimentalTime
fun main() {
    Mocker(kafkaPort = 9092)
}

@ExperimentalTime
fun Mocker(kafkaPort: Int) {
    val producer = inputTopic.producer(kafkaProperties(kafkaPort))

    runBlocking {
        while (true) {
            producer.write(Data(value1 = Random.nextInt(0, 10), value2 = Random.nextInt(40, 50)), flush = true)
            delay(1.seconds)
        }
    }
}

private fun <T> Topic<T>.producer(properties: Properties): Producer<T> {
    val producer = KafkaProducer<String, String>(properties)
    return Producer { data, flush ->
        producer.send(ProducerRecord(name, Json.encodeToString(serializer, data)))
        if (flush) {
            producer.flush()
        }
    }
}

fun interface Producer<T> {
    fun write(data: T, flush: Boolean)
}
