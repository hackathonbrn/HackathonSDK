package ru.nofeature.hackathon

import io.ktor.client.*
import io.ktor.client.engine.js.*

actual object Client {
    actual fun provide(): HttpClient = HttpClient(Js)
}