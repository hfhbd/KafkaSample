
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.json.*

suspend fun <T> ApplicationCall.respondJson(
    serializer: KSerializer<T>,
    json: Json = Json,
    data: suspend ApplicationCall.() -> T
) =
    respondText(contentType = ContentType.Application.Json) {
        json.encodeToString(serializer, data())
    }

suspend fun <T> ApplicationCall.respondJsonList(
    serializer: KSerializer<T>,
    json: Json = Json,
    data: suspend ApplicationCall.() -> List<T>
) =
    respondText(contentType = ContentType.Application.Json) {
        json.encodeToString(ListSerializer(serializer), data())
    }
