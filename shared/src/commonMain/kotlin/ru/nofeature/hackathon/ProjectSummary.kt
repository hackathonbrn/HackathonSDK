package ru.nofeature.hackathon

import kotlinx.serialization.Serializable


@Serializable
data class ProjectSummary(
    val projectName: String,
    val teamName: String,
    val participants: List<String>,
    val averageScores: Map<String, Double>,
    val totalScore: Double,
    val judges: Set<String>
)