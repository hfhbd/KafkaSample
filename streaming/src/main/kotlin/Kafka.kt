import Record.Companion.at
import kotlinx.datetime.*
import org.testcontainers.containers.*
import org.testcontainers.utility.*
import kotlin.coroutines.*

suspend fun Service(kafka: Address, clock: Clock, action: suspend (Data) -> Classified) {
    OutputTopic(kafka).produce {
        InputTopic(kafka).collect {
            emit(action(it) at clock.now())
        }
    }
}

suspend fun useTestContainer(): Address = suspendCoroutine {
    val image = DockerImageName.parse("confluentinc/cp-kafka:6.2.0")
    val container = KafkaContainer(image)

    container.start()
    it.resume(Address(container.host, container.firstMappedPort))
}

fun InputTopic(kafkaAddress: Address) = KafkaTopic(
    "input-data",
    serializer = Data.serializer(),
    kafkaAddress = kafkaAddress
)

fun OutputTopic(kafkaAddress: Address) = KafkaTopic(
    "output-data",
    serializer = Classified.serializer(),
    kafkaAddress = kafkaAddress
)
