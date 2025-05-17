package ru.nofeature.hackathon.evaluate.impl

import kotlinx.serialization.Serializable
import ru.nofeature.hackathon.evaluate.api.ProjectRating

@Serializable
data class SimpleProjectRating(
    override val project: SimpleProject,
    override val judge: SimpleJudge,
    override val ratings: List<SimpleRating>,
) : ProjectRating
