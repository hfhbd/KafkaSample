import io.ktor.resources.*
import kotlinx.serialization.*

@Serializable
@Resource("/input")
class Input

@Serializable
@Resource("/output")
class Output
