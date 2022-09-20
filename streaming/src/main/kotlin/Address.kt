data class Address(val host: String, val port: Int) {
    override fun toString(): String = "$host:$port"

    companion object
}
