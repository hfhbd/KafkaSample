import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import org.jetbrains.compose.web.dom.*

@Composable
fun Navbar() {
    Nav(attrs = {
        classes("navbar", "navbar-expand-${Breakpoint.Medium}", "navbar-${Color.Dark}", "sticky-top", "bg-${Color.Dark}")
    }) {
        Container(type = Breakpoint.ExtraExtraLarge) {
            Brand {
                Text("KafkaDemo")
            }
        }
    }
}
