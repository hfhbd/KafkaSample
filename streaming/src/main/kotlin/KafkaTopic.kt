import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.json.*
import org.apache.kafka.clients.admin.*
import org.apache.kafka.clients.consumer.*
import org.apache.kafka.clients.producer.*
import org.apache.kafka.common.config.*
import org.apache.kafka.common.serialization.*
import org.apache.kafka.common.utils.*
import org.apache.kafka.streams.*
import org.apache.kafka.streams.kstream.*
import org.apache.kafka.streams.state.*
import java.util.*
import kotlin.coroutines.*
import kotlin.time.*
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
class KafkaTopic<T>(
    val name: String,
    val serializer: KSerializer<T>,
    val kafkaAddress: Address,
    createIfMissing: Boolean = true
) : AbstractFlow<T>() {
    init {
        if (createIfMissing) {
            val properties = kafkaAddress.commonProperties
            val adminClient = AdminClient.create(properties)

            val numberOfPartitions = 1
            val replicationFactor: Short = 1
            val newTopic = NewTopic(name, numberOfPartitions, replicationFactor)

            newTopic.configs(
                mapOf(
                    TopicConfig.RETENTION_MS_CONFIG to (7.days).inWholeMilliseconds.toString(),
                    TopicConfig.RETENTION_BYTES_CONFIG to (-1).toString()
                )
            )

            adminClient.createTopics(listOf(newTopic))
            adminClient.close()
        }
    }

    @FlowPreview
    override suspend fun collectSafely(collector: FlowCollector<T>) {
        callbackFlow {
            val builder = StreamsBuilder()
            val stream: KStream<String, String> = builder.stream(name)

            stream.foreach { _, value: String ->
                val input = Json.decodeFromString(serializer, value)
                trySendBlocking(input)
            }

            val streams = KafkaStreams(
                builder.build(), kafkaAddress.streamProperties()
            )
            streams.start()

            awaitClose {
                streams.close(Duration.ZERO.toJavaDuration())
            }
        }.collect(collector)
    }

    suspend fun produce(action: suspend FlowCollector<Record<T>>.() -> Unit) {
        val properties = kafkaAddress.commonProperties.apply {
            this[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer().javaClass
            this[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer().javaClass
        }
        return KafkaProducer<String, String>(properties).use { producer ->
            flow {
                action()
            }.collect {
                val dataJson = Json.encodeToString(serializer, it.value)
                producer.send(ProducerRecord(name, null, it.timestamp.toEpochMilliseconds(), null, dataJson))
                producer.flush()
            }
        }
    }

    fun windowing(groupBy: (T) -> String, window: Duration): Flow<List<T>> =
        callbackFlow {
            val builder = StreamsBuilder()
            builder.stream<String, String>(name).groupBy { _, value: String ->
                val input = Json.decodeFromString(serializer, value)
                groupBy(input)
            }.windowedBy(TimeWindows.ofSizeWithNoGrace(window.toJavaDuration())).aggregate(
                { Json.encodeToString(ListSerializer(serializer), emptyList()) },
                { _, newValue: String, listValues: String ->
                    val newInput = Json.decodeFromString(serializer, newValue)
                    val oldInputs = Json.decodeFromString(ListSerializer(serializer), listValues)
                    val new = oldInputs + newInput
                    Json.encodeToString(ListSerializer(serializer), new)
                },
                // https://issues.apache.org/jira/browse/KAFKA-9259
                Materialized.with<String, String, WindowStore<Bytes, ByteArray>>(
                    Serdes.StringSerde(), Serdes.StringSerde()
                ).withLoggingDisabled()
            ).suppress(
                Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded().withLoggingDisabled())
            ).toStream { key, _ ->
                key.key()
            }.foreach { _, listValuesString: String ->
                val windowedInputs = Json.decodeFromString(ListSerializer(serializer), listValuesString)
                trySendBlocking(windowedInputs)
            }

            val streams = KafkaStreams(builder.build(), kafkaAddress.streamProperties())
            streams.start()

            awaitClose {
                streams.close(Duration.ZERO.toJavaDuration())
            }
        }

    /**
     * Creates a **cold** flow using Kafka Consumer.
     */
    fun consumeFlow(offsetStart: ConsumeStart = ConsumeStart.Earliest): Flow<List<T>> = flow {
        val properties = kafkaAddress.commonProperties.apply {
            this[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer().javaClass
            this[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer().javaClass
            this[ConsumerConfig.GROUP_ID_CONFIG] = UUID.randomUUID().toString()
            this[ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG] = false
            this[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = false
            this[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = offsetStart.toString()
        }
        KafkaConsumer<String, String>(properties).use { kafkaConsumer ->
            kafkaConsumer.subscribe(listOf(name))

            val initMetadataPoll = kafkaConsumer.poll(serializer)
            if (initMetadataPoll.isNotEmpty()) {
                kafkaConsumer.commit()
                emit(initMetadataPoll)
            }

            while (true) {
                val data = kafkaConsumer.poll(serializer)
                kafkaConsumer.commit()
                emit(data)
            }
        }
    }
}

enum class ConsumeStart(private val value: String) {
    Earliest("earliest"), Latest("latest");

    override fun toString(): String = value
}

private fun Address.streamProperties(
    applicationID: String = UUID.randomUUID().toString()
) = commonProperties.apply {
    this[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.StringSerde().javaClass
    this[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.StringSerde().javaClass
    this[StreamsConfig.APPLICATION_ID_CONFIG] = applicationID
    this[StreamsConfig.COMMIT_INTERVAL_MS_CONFIG] = 100
}

private const val DEFAULT_KAFKA_PORT = 9092

val Address.Companion.defaultKafka: Address
    get() = Address(
        host = System.getenv("KAFKA_HOST").takeUnless { it.isEmpty() }
            ?: error("KAFKA_HOST not given as environment variable"),
        port = System.getenv("KAFKA_PORT")?.toInt() ?: DEFAULT_KAFKA_PORT
    )

/**
 * Returns the common properties used by the streaming, consumer and producer Kafka implementations.
 */
private inline val Address.commonProperties: Properties
    get() = Properties().apply {
        this[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = this@commonProperties.toString()
    }

private suspend fun <K, V> KafkaConsumer<K, V>.commit() = suspendCoroutine<Unit> {
    commitSync()
    it.resume(Unit)
}

private suspend fun <K, V> KafkaConsumer<K, String>.poll(serializer: KSerializer<V>) =
    suspendCancellableCoroutine<List<V>> {
        it.invokeOnCancellation { close() }
        val poll = poll(POLL_DURATION.milliseconds.toJavaDuration())
        it.resume(
            poll.map { entry: ConsumerRecord<K, String> ->
                Json.decodeFromString(serializer, entry.value())
            }
        )
    }

private const val POLL_DURATION = 100
