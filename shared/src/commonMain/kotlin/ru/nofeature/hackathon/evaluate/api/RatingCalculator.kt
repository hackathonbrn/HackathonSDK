package ru.nofeature.hackathon.evaluate.api

interface RatingCalculator {
    fun calculateTotalScore(ratings: List<ProjectRating>): Map<Project, Double>
    fun calculateAverageScores(ratings: List<ProjectRating>): Map<Project, Map<Criterion, Double>>
}