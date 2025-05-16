package ru.nofeature.hackathon.evaluate.impl

import ru.nofeature.hackathon.evaluate.api.Judge
import ru.nofeature.hackathon.evaluate.api.Project
import ru.nofeature.hackathon.evaluate.api.ProjectRating
import ru.nofeature.hackathon.evaluate.api.Rating
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class SimpleProjectRating @OptIn(ExperimentalTime::class) constructor(
    override val project: Project,
    override val judge: Judge,
    override val ratings: List<Rating>,
    override val timestamp: Long = Clock.System.now().toEpochMilliseconds()
) : ProjectRating {
    init {
        require(ratings.isNotEmpty()) { "At least one rating is required" }
    }
}