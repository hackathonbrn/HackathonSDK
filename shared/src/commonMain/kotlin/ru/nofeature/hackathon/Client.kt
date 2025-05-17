package ru.nofeature.hackathon

import io.ktor.client.*

expect object Client {
    fun provide() : HttpClient
}