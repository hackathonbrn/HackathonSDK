package ru.nofeature.hackathon.evaluate.impl

import ru.nofeature.hackathon.evaluate.api.Judge

data class SimpleJudge(
    override val id: String,
    override val name: String
) : Judge
