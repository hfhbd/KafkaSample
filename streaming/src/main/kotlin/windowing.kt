import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.time.*

fun <T> Flow<T>.windowing(groupBy: (T) -> String, window: Duration): Flow<List<T>> = if (this is KafkaTopic)
    windowing(groupBy, window)
else flow {
    val groups = mutableMapOf<String, TimeGroup<T>>()
    coroutineScope {
        collect { value ->
            val iterator = groups.iterator()
            iterator.forEach { (_, values) ->
                val passed = values.sendAfter.isCompleted
                if (passed) {
                    emit(values.values)
                    iterator.remove()
                }
            }

            val key = groupBy(value)
            if (groups.containsKey(key)) {
                val group = groups[key]!!
                groups[key] = group.copy(values = group.values + value)
            } else {
                groups[key] = TimeGroup(launch { delay(window) }, listOf(value))
            }
        }
    }
}

private data class TimeGroup<T>(val sendAfter: Job, val values: List<T>)
