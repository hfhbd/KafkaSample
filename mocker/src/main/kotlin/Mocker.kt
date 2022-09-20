
import Record.Companion.at
import kotlinx.coroutines.*
import kotlinx.datetime.*
import kotlin.random.*
import kotlin.time.Duration.Companion.milliseconds

suspend fun main() {
    Mocker(Address("localhost", 9092), clock = Clock.System)
}

suspend fun Mocker(kafka: Address, clock: Clock) {
    val inputTopic = InputTopic(kafka)
    inputTopic.produce {

        while (true) {
            val now = clock.now()
            emit(Data(value1 = Random.nextInt(0, 10), value2 = Random.nextInt(40, 50), now) at now)
            delay(100.milliseconds)
        }
    }
}
