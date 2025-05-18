package ru.nofeature.hackathon.evaluate.impl

import ru.nofeature.hackathon.evaluate.api.*
import ru.nofeature.hackathon.net.Ktor

class InMemoryRatingRepository : RatingRepository {

    private val ratings = mutableListOf<ProjectRating>()

    override suspend fun getCriteria(): List<SimpleCriterion> = Ktor.loadCriterion()

    fun createCriteria(criterion: SimpleCriterion) = Ktor.createCriterion(
        criterion
    )
    fun deleteCriteria(criterion: SimpleCriterion) = Ktor.deleteCriterion(
        criterion
    )

    override suspend fun submitRating(rating: ProjectRating): Result<Unit> {
        ratings.removeAll { it.project == rating.project && it.judge == rating.judge }
        ratings.add(rating)
        return Result.success(Unit)
    }

    private var smallCache : List<ProjectWithTeam> = emptyList()

    override suspend fun getProjects(): List<ProjectWithTeam> = Ktor.loadTeams().filter {
        it.project.isNotEmpty()
    }.map {
        ProjectWithTeam(it.project, it.command)
    }.toSet().toList().also {
        smallCache = it
    }

    var judge: SimpleJudge? = null
}