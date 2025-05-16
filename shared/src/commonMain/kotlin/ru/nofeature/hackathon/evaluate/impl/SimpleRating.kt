package ru.nofeature.hackathon.evaluate.impl

import ru.nofeature.hackathon.evaluate.api.Criterion
import ru.nofeature.hackathon.evaluate.api.Rating

data class SimpleRating(
    override val criterion: Criterion,
    override val score: Double,
) : Rating {
    init {
        require(score in 0.0..criterion.maxScore) {
            "Score must be between 0 and ${criterion.maxScore}"
        }
    }
}