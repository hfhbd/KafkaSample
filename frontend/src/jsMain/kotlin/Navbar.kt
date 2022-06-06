import androidx.compose.runtime.*
import app.softwork.bootstrapcompose.*
import org.jetbrains.compose.web.dom.*

@Composable
fun Navbar() {
    Navbar(
        collapseBehavior = NavbarCollapseBehavior.AtBreakpoint(Breakpoint.Medium),
        colorScheme = Color.Dark,
        placement = NavbarPlacement.StickyTop,
        backgroundColor = Color.Dark,
        containerBreakpoint = Breakpoint.ExtraExtraLarge
    ) {
        Brand {
            Text("KafkaDemo")
        }
    }
}
