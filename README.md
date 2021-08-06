# Kafka Sample Project with Ktor

## Mocker

This module sends every second a new data to an input topic using `Kafka Producer`.

## Streaming

This module subscripes the input topic and calls for every new data a converter, which returns a converted result. The
converted result is written to an output topic. It uses `Kafka Streaming` and a `Testcontainer` Kafka instance.

## Converter

This module contains a (static) converter to convert an input data to some ouput data.

## Backend

A `Ktor` server using `Kafka Consumer` to poll new data and return them as a list to the frondend.

## Frontend

A `Compose for Web` based frontend displaying the data from the backend.

## Demo

A shortcut to launch the Backend and the Kafka instance with one click.

