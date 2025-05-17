package ru.nofeature.hackathon.evaluate.impl

import kotlinx.serialization.Serializable
import ru.nofeature.hackathon.evaluate.api.Judge

@Serializable
data class SimpleJudge(
    override val name: String
) : Judge
