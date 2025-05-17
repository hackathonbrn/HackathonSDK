package ru.nofeature.hackathon

import io.ktor.client.*

actual object Client {
    actual fun provide(): HttpClient = HttpClient()
}