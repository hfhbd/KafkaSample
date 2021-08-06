fun interface Converter<T, R> {
    suspend operator fun invoke(data: T): R
}
