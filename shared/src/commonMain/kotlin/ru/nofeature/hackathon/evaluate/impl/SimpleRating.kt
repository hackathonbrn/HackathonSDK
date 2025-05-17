package ru.nofeature.hackathon.evaluate.impl

import kotlinx.serialization.Serializable
import ru.nofeature.hackathon.evaluate.api.Rating

@Serializable
data class SimpleRating(
    override val criterion: SimpleCriterion,
    override val score: Double,
) : Rating