import kotlinx.serialization.*

object Routes {
    data class Get<Out>(val path: String, val serializer: KSerializer<Out>, )

    val input = Get("/input", Data.serializer())
    val output = Get("/output", Classified.serializer())
}
