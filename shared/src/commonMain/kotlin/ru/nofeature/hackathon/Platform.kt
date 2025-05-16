package ru.nofeature.hackathon

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform