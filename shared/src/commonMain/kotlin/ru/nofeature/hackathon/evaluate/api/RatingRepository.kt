package ru.nofeature.hackathon.evaluate.api

import ru.nofeature.hackathon.evaluate.impl.ProjectWithTeam

interface RatingRepository {
    suspend fun getCriteria(): List<Criterion>
    suspend fun submitRating(rating: ProjectRating): Result<Unit>
    suspend fun getProjects(): List<ProjectWithTeam>
}