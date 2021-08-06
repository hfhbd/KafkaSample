import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.json.*
import org.jetbrains.compose.web.*
import org.jetbrains.compose.web.dom.*

val scope = MainScope()

fun main() {
    renderComposable(rootElementId = "root") {
        MainApp(backendPort = 8888)
    }
}

@Composable
fun MainApp(backendPort: Int) {
    val client = HttpClient(Js) {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTP
                host = "localhost"
                port = backendPort
            }
        }
    }
    Navbar()

    Container {
        var data by remember { mutableStateOf(emptyList<Classified>()) }

        Button("Refresh") {
            scope.launch {
                data = Routes.output by client.get(urlString = Routes.output.path)
            }
        }

        DataTable(data)
    }
}

private infix fun <T> Routes.Get<T>.by(string: String): List<T> =
    Json.decodeFromString(ListSerializer(serializer), string)

@Composable
private fun DataTable(data: List<Classified>) {
    Table(data) { index, it ->
        column("Value 1") {
            Text(it.originalData.value1.toString())
        }
        column("Value 2") {
            Text(it.originalData.value2.toString())
        }
        column("Status") {
            Text(it.classifier.name)
        }
        column("Model Name") {
            Text(it.modelName)
        }
    }
}
