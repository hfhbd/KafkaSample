import kotlinx.coroutines.*
import kotlinx.datetime.*

suspend fun main() = coroutineScope {
    val kafka = useTestContainer()

    val clock = Clock.System

    launch {
        Backend(kafka)
    }
    launch {
        Mocker(kafka, clock)
    }
    // You must start the frontend by yourself (:frontend:browserProductionRun)

    Service(kafka, clock) {
        Classified(
            modelName = "StaticConverter",
            originalData = it,
            classifier = Classifier.Healthy,
            modifiedDate = clock.now()
        )
    }
}
