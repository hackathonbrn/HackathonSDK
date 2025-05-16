package ru.nofeature.hackathon.evaluate.impl

import ru.nofeature.hackathon.evaluate.api.Criterion
import ru.nofeature.hackathon.evaluate.api.Project
import ru.nofeature.hackathon.evaluate.api.ProjectRating
import ru.nofeature.hackathon.evaluate.api.RatingCalculator

class SimpleRatingCalculator : RatingCalculator {
    override fun calculateTotalScore(ratings: List<ProjectRating>): Map<Project, Double> {
        return ratings.groupBy { it.project }
            .mapValues { (_, projectRatings) ->
                projectRatings.flatMap { it.ratings }
                    .sumOf { it.score }
            }
    }

    override fun calculateAverageScores(ratings: List<ProjectRating>): Map<Project, Map<Criterion, Double>> {
        return ratings.groupBy { it.project }
            .mapValues { (_, projectRatings) ->
                projectRatings.flatMap { it.ratings }
                    .groupBy { it.criterion }
                    .mapValues { (_, criterionRatings) ->
                        criterionRatings.map { it.score }.average()
                    }
            }
    }
}