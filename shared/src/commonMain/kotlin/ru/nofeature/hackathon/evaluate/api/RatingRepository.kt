package ru.nofeature.hackathon.evaluate.api

interface RatingRepository {
    fun getCriteria(): List<Criterion>
    suspend fun submitRating(rating: ProjectRating): Result<Unit>
    suspend fun getExistingRating(project: Project, judgeId: Judge): ProjectRating?
    suspend fun getProjects(): List<Project>
}