package ru.nofeature.hackathon.data

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nofeature.hackathon.evaluate.impl.SimpleCriterion
import ru.nofeature.hackathon.evaluate.impl.SimpleJudge
import ru.nofeature.hackathon.evaluate.impl.SimpleProject
import ru.nofeature.hackathon.evaluate.impl.SimpleProjectRating
import ru.nofeature.hackathon.evaluate.impl.SimpleRating

object SimpleEvaluations : Table("evaluations") {
    val jujde = varchar("judge", 128)
    val project = varchar("project", 128)
    val score = float("score")
    val criteria = varchar("criteria", 128)
}

fun evaluateFromDb() : List<SimpleProjectRating> = transaction {
    SimpleEvaluations.selectAll().groupBy {
        Pair(it[SimpleEvaluations.project], it[SimpleEvaluations.jujde])
    }.map { it ->
        SimpleProjectRating(
            project = SimpleProject(it.key.first),
            judge = SimpleJudge(it.key.second),
            ratings = it.value.map { row ->
                SimpleRating(
                    SimpleCriterion(row[SimpleEvaluations.criteria], "", 10.0),
                    score = row[SimpleEvaluations.score].toDouble()
                )
            }
        )
    }
}