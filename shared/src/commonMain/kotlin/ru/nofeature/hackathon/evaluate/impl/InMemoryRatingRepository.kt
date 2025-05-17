package ru.nofeature.hackathon.evaluate.impl

import ru.nofeature.hackathon.evaluate.api.*
import ru.nofeature.hackathon.net.Ktor

class InMemoryRatingRepository : RatingRepository {
    private val criteria = listOf(
        SimpleCriterion("Инновационность", "Оригинальность решения", 10.0),
        SimpleCriterion("Технологичность", "Качество технической реализации", 10.0),
        SimpleCriterion("Дизайн", "Качество UI/UX ", 10.0),
        SimpleCriterion("Практичность", "Реальная применимость", 10.0)
    )

    private val ratings = mutableListOf<ProjectRating>()

    override fun getCriteria(): List<Criterion> = criteria

    override suspend fun submitRating(rating: ProjectRating): Result<Unit> {
        ratings.removeAll { it.project == rating.project && it.judge == rating.judge }
        ratings.add(rating)
        return Result.success(Unit)
    }

    override suspend fun getExistingRating(project: Project, judge: Judge): ProjectRating? =
        ratings.firstOrNull { it.project == project && it.judge == judge }

    override suspend fun getProjects(): List<Project> = Ktor.loadTeams().filter {
        it.project.isNotEmpty()
    }.map {
        SimpleProject(it.project)
    }.toSet().toList()

    var judge: SimpleJudge? = null
}

