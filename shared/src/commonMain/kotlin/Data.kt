
import kotlinx.datetime.*
import kotlinx.serialization.*

@Serializable
data class Data(val value1: Int, val value2: Int, val creationTime: Instant)
