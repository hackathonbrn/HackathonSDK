package ru.nofeature.hackathon.evaluate.impl

import ru.nofeature.hackathon.evaluate.api.Criterion
import ru.nofeature.hackathon.evaluate.api.Judge
import ru.nofeature.hackathon.evaluate.api.Project
import ru.nofeature.hackathon.evaluate.api.ProjectRating
import ru.nofeature.hackathon.evaluate.api.RatingRepository

class InMemoryRatingRepository : RatingRepository {
    private val criteria = listOf(
        SimpleCriterion("Innovation", "Originality of the solution", 10.0),
        SimpleCriterion("Technical", "Technical implementation quality", 10.0),
        SimpleCriterion("Design", "UI/UX design quality", 8.0),
        SimpleCriterion("Practicality", "Real-world applicability", 6.0)
    )

    private val projects = listOf(
        SimpleProject("1", "EcoTracker"),
        SimpleProject("2", "HealthMonitor"),
        SimpleProject("3", "SmartPark")
    )

    private val judges = listOf(
        SimpleJudge("j1", "Alice Smith"),
        SimpleJudge("j2", "Bob Johnson")
    )

    private val ratings = mutableListOf<ProjectRating>()

    override fun getCriteria(): List<Criterion> = criteria

    override suspend fun getProjectsToJudge(judgeId: String): List<Project> = projects

    override suspend fun getJudge(judgeId: String): Judge =
        judges.first { it.id == judgeId }

    override suspend fun submitRating(rating: ProjectRating): Result<Unit> {
        ratings.removeAll { it.project.id == rating.project.id && it.judge.id == rating.judge.id }
        ratings.add(rating)
        return Result.success(Unit)
    }

    override suspend fun getExistingRating(projectId: String, judgeId: String): ProjectRating? =
        ratings.firstOrNull { it.project.id == projectId && it.judge.id == judgeId }
}