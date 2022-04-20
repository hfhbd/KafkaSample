import kotlinx.serialization.*
import kotlin.js.*

@Serializable
data class Classified(val modelName: String, val originalData: Data, val classifier: Classifier)
