package ru.nofeature.hackathon.evaluate

import ru.nofeature.hackathon.evaluate.impl.SimpleCriterion
import ru.nofeature.hackathon.evaluate.impl.SimpleJudge
import ru.nofeature.hackathon.evaluate.impl.SimpleProject
import ru.nofeature.hackathon.evaluate.impl.SimpleProjectRating
import ru.nofeature.hackathon.evaluate.impl.SimpleRating
import ru.nofeature.hackathon.evaluate.impl.SimpleRatingCalculator
import kotlin.test.Test

class EvaluateTest {
    @Test
    fun startTest() {
        // Создаем критерии оценки
        val innovationCriterion = SimpleCriterion(
            "Innovation",
            "How innovative is the solution?",
            10.0
        )
        val usabilityCriterion = SimpleCriterion(
            "Usability",
            "How user-friendly is the solution?",
            8.0
        )

        // Создаем жюри
        val judge1 = SimpleJudge("j1", "Alice")
        val judge2 = SimpleJudge("j2", "Bob")

        // Создаем проекты
        val projectA = SimpleProject("p1", "EcoTracker")
        val projectB = SimpleProject("p2", "HealthMonitor")

        // Создаем оценки
        val rating1 = SimpleProjectRating(
            projectA,
            judge1,
            listOf(
                SimpleRating(innovationCriterion, 8.5, "Good idea but not completely new"),
                SimpleRating(usabilityCriterion, 7.0, "UI needs improvement")
            )
        )

        val rating2 = SimpleProjectRating(
            projectA,
            judge2,
            listOf(
                SimpleRating(innovationCriterion, 9.0),
                SimpleRating(usabilityCriterion, 6.5)
            )
        )

        val rating3 = SimpleProjectRating(
            projectB,
            judge1,
            listOf(
                SimpleRating(innovationCriterion, 7.0),
                SimpleRating(usabilityCriterion, 8.0)
            )
        )

        // Рассчитываем результаты
        val calculator = SimpleRatingCalculator()
        val allRatings = listOf(rating1, rating2, rating3)

        val totalScores = calculator.calculateTotalScore(allRatings)
        val averageScores = calculator.calculateAverageScores(allRatings)

        println("Total scores: $totalScores")
        println("Average scores: $averageScores")
    }
}