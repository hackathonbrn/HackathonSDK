package ru.nofeature.hackathon.evaluate.api

interface Criterion {
    val name: String
    val description: String
    val maxScore: Double
}