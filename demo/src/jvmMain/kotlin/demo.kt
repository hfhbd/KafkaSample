import kotlinx.coroutines.*
import kotlin.time.*

@ExperimentalTime
suspend fun main() = coroutineScope {
    KafkaStreaming(converter = StaticConverter())
        .start(this) { port ->
            launch {
                Backend(kafkaPort = port)
            }
            launch {
                Mocker(kafkaPort = port)
            }
            // You must start the frontend by yourself
        }
}
