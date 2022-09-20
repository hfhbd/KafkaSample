import kotlinx.datetime.*

data class Record<T>(val value: T, val timestamp: Instant) {
    companion object {
        infix fun <T> T.at(timestamp: Instant): Record<T> = Record(this, timestamp)
    }
}
