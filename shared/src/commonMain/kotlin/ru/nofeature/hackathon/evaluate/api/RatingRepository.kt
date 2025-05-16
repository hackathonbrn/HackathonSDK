package ru.nofeature.hackathon.evaluate.api

interface RatingRepository {
    fun getCriteria(): List<Criterion>
    suspend fun getProjectsToJudge(judgeId: String): List<Project>
    suspend fun getJudge(judgeId: String): Judge
    suspend fun submitRating(rating: ProjectRating): Result<Unit>
    suspend fun getExistingRating(projectId: String, judgeId: String): ProjectRating?
}