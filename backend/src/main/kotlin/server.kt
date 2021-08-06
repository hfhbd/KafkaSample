import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import kotlinx.serialization.json.*
import org.apache.kafka.clients.consumer.*
import java.util.*
import kotlin.coroutines.*
import kotlin.time.*


@ExperimentalTime
fun main() {
    Backend(9092)
}

@ExperimentalTime
private fun <T> Topic<T>.consumer(properties: Properties): Consumer<T> {
    val consumer = KafkaConsumer<String, String>(properties)
    consumer.subscribe(listOf(name))
    return Consumer { pollTimeout ->
        suspendCoroutine {
            val results = consumer.poll(pollTimeout.toJavaDuration())
            it.resume(results.map {
                Json.decodeFromString(serializer, it.value())
            })
        }
    }
}

@ExperimentalTime
fun interface Consumer<T> {
    suspend fun poll(pollTimeout: Duration): List<T>
}

@ExperimentalTime
private fun Application.kafkaViewer(port: Int) {
    val properties = kafkaProperties(port)
    routing {
        route(Routes.output.path) {
            val consumer = outputTopic.consumer(properties)
            get {
                call.respondJsonList(Routes.output.serializer) {
                    consumer.poll(Duration.seconds(1))
                }
            }
        }

        route(Routes.input.path) {
            val consumer = inputTopic.consumer(properties)

            get {
                call.respondJsonList(Routes.input.serializer) {
                    consumer.poll(Duration.seconds(1))
                }
            }
        }
    }
}

@ExperimentalTime
fun Backend(kafkaPort: Int) {
    embeddedServer(CIO, port = 8888) {
        kafkaViewer(port = kafkaPort)

        install(CORS) {
            anyHost()
        }

        routing {
            get("/") {
                call.respondText { "Hello World" }
            }
        }
    }.start(wait = true)
}
