import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import kotlinx.coroutines.*
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

        install(Resources)
    }
    Navbar()

    Container {
        var data by remember { mutableStateOf(emptyList<Classified>()) }

        Button("Refresh") {
            scope.launch {
                data = client.get(Output()).body()
            }
        }

        DataTable(data)
    }
}

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
