import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import org.apache.kafka.clients.admin.*
import org.apache.kafka.common.serialization.*
import org.apache.kafka.streams.*
import org.apache.kafka.streams.kstream.*
import org.testcontainers.containers.*
import org.testcontainers.utility.*
import java.util.*
import kotlin.time.*

suspend fun main() = coroutineScope {
    KafkaStreaming {
        Classified("Dummy", originalData = it, classifier = Classifier.Healthy)
    }.start(this) {
        println("Started on port $it")
    }
}

private fun useTestContainer(): Pair<String, Int> {
    val kafkaServer = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.0")).apply {
        start()
    }
    return kafkaServer.host to kafkaServer.firstMappedPort
}

class NewTopConfig {
    var retentionTime: Duration? = null
    var retentionBytes: Long? = null

    fun build() = mapOf(
        "retention.ms" to (retentionTime?.inWholeMilliseconds ?: -1).toString(),
        "retention.bytes" to (retentionBytes ?: -1).toString()
    )
}

fun <T> AdminClient.create(
    topic: Topic<T>,
    numPartitions: Int = 1,
    replicationFactor: Short = 1,
    config: NewTopConfig.() -> Unit = { }
) {
    createTopics(listOf(NewTopic(topic.name, numPartitions, replicationFactor).apply {
        configs(NewTopConfig().apply(config).build())
    }))
}

fun <T, Out> Topic<T>.map(output: Topic<Out>, properties: Properties, block: suspend (T) -> Out) {
    val builder = StreamsBuilder()
    val textLines = builder.stream<String, String>(name)

    val classifiedData: KStream<String, String> = textLines.mapValues { _, input ->
            val data = Json.decodeFromString(serializer, input)
            val out = runBlocking(Dispatchers.Default) { block(data) }
            Json.encodeToString(output.serializer, out)
        }
    classifiedData.to(outputTopic.name)

    val streams = KafkaStreams(builder.build(), properties)
    streams.start()
}


class KafkaStreaming(private val converter: Converter<Data, Classified>) {
    suspend fun start(scope: CoroutineScope, onStart: suspend (Int) -> Unit) {
        val (host, port) = useTestContainer()

        val props = Properties().apply {
            put(StreamsConfig.APPLICATION_ID_CONFIG, "kafkaDemo")
            put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "$host:$port")
            put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde().javaClass)
            put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde().javaClass)
        }

        AdminClient.create(props).use {
            it.create(inputTopic)
            it.create(outputTopic)
        }

        val streamingJob = scope.async {
            inputTopic.map(outputTopic, kafkaProperties(port)) {
                converter(it)
            }
        }

        withContext(Dispatchers.Default) {
            scope.launch {
                onStart(port)
            }
        }
        streamingJob.await()
    }
}
