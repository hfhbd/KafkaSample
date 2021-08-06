import kotlinx.serialization.*
import kotlin.js.*

@Serializable
@JsExport
data class Data(val value1: Int, val value2: Int)
