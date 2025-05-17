package ru.nofeature.hackathon

import ru.nofeature.hackathon.data.evaluateFromDb
import ru.nofeature.hackathon.data.teamFromDb

fun generateReport() : List<ProjectSummary>{
    val projectRatings = evaluateFromDb()
    val participants = teamFromDb()

    // Группируем участников по проектам
    val participantsByProject = participants
        .filter { it.role == "Участник" && it.project.isNotBlank() }
        .groupBy { it.project }

    // Группируем оценки по проектам
    val ratingsByProject = projectRatings.groupBy { it.project.name }

    // Создаем список критериев
    val criteria = projectRatings
        .flatMap { it.ratings.map { rating -> rating.criterion.name } }
        .distinct()

    // Формируем отчет по проектам
    val report = ratingsByProject.map { (projectName, ratings) ->
        // Участники проекта
        val projectParticipants = participantsByProject[projectName] ?: emptyList()
        val teamName = projectParticipants.firstOrNull()?.command ?: ""

        // Средние оценки по критериям
        val criterionScores = criteria.associateWith { criterionName ->
            val scores = ratings.flatMap { projectRating ->
                projectRating.ratings
                    .filter { it.criterion.name == criterionName }
                    .map { it.score }
            }
            if (scores.isEmpty()) 0.0 else scores.average()
        }

        // Общий балл
        val totalScore = criterionScores.values.sum()

        // Судьи, оценивавшие проект
        val judges = ratings.map { it.judge.name }.toSet()

        ProjectSummary(
            projectName = projectName,
            teamName = teamName,
            participants = projectParticipants.map { it.name },
            averageScores = criterionScores,
            totalScore = totalScore,
            judges = judges
        )
    }.sortedByDescending { it.totalScore }
    return report
}