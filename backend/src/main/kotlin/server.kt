
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
import kotlinx.coroutines.flow.*
import kotlin.time.Duration.Companion.seconds


suspend fun main() = coroutineScope {
    Backend(Address("localhost", 9092))
}

private fun Application.kafkaViewer(kafka: Address) {

    routing {
        val outputConsumer = OutputTopic(kafka).consumeFlow(ConsumeStart.Latest)
        get<Output> {
            val output = outputConsumer.take(10).toList().flatten()
            call.respond(output)
        }

        val inputConsumer = InputTopic(kafka).consumeFlow(ConsumeStart.Latest)
        get<Input> {
            val input = inputConsumer.onEach {
                delay(1.seconds)
            }.take(5).toList()
            call.respond(input)
        }
    }
}

fun CoroutineScope.Backend(kafka: Address) {
    embeddedServer(CIO, port = 8888) {
        install(CORS) {
            anyHost()
        }

        install(ContentNegotiation) {
            json()
        }
        install(Resources)

        kafkaViewer(kafka)

        routing {
            get("/") {
                call.respondText { "Hello World" }
            }
        }
    }.start(wait = true)
}
