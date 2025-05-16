package ru.nofeature.hackathon.evaluate.api

interface Rating {
    val criterion: Criterion
    val score: Double
}