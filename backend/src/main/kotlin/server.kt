import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import org.apache.kafka.clients.consumer.*
import java.util.*
import kotlin.coroutines.*
import kotlin.time.*
import kotlin.time.Duration.Companion.seconds


suspend fun main() = coroutineScope {
    Backend(9092)
}

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

fun interface Consumer<T> {
    suspend fun poll(pollTimeout: Duration): List<T>
}

private fun Application.kafkaViewer(port: Int) {
    val properties = kafkaProperties(port)

    routing {
        val outputConsumer = outputTopic.consumer(properties)
        get<Output> {
            call.respond(outputConsumer.poll(1.seconds))
        }

        val inputConsumer = inputTopic.consumer(properties)
        get<Input> {
            call.respond(inputConsumer.poll(1.seconds))
        }
    }
}

fun CoroutineScope.Backend(kafkaPort: Int) {
    embeddedServer(CIO, port = 8888) {
        install(CORS) {
            anyHost()
        }

        install(ContentNegotiation) {
            json()
        }
        install(Resources)

        kafkaViewer(port = kafkaPort)

        routing {
            get("/") {
                call.respondText { "Hello World" }
            }
        }
    }.start(wait = true)
}
