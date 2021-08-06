class StaticConverter : Converter<Data, Classified> {
    override suspend operator fun invoke(data: Data): Classified {
        return Classified(
            modelName = "StaticConverter",
            originalData = data,
            classifier = Classifier.Healthy
        )
    }
}
