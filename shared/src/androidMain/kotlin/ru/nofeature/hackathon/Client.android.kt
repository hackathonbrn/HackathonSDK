package ru.nofeature.hackathon

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

actual object Client {
    actual fun provide(): HttpClient = HttpClient(OkHttp)
}