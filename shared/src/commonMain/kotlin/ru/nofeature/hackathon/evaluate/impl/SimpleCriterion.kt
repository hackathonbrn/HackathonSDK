package ru.nofeature.hackathon.evaluate.impl

import kotlinx.serialization.Serializable
import ru.nofeature.hackathon.evaluate.api.Criterion

@Serializable
data class SimpleCriterion(
    override val name: String,
    override val description: String,
    override val maxScore: Double
) : Criterion