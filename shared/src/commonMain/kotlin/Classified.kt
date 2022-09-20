import kotlinx.serialization.*
import kotlinx.datetime.*
import kotlin.js.*

@Serializable
data class Classified(val modelName: String, val originalData: Data, val classifier: Classifier, val modifiedDate: Instant)
