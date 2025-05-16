package ru.nofeature.hackathon.evaluate.impl

import ru.nofeature.hackathon.evaluate.api.Criterion

data class SimpleCriterion(
    override val name: String,
    override val description: String,
    override val maxScore: Double
) : Criterion