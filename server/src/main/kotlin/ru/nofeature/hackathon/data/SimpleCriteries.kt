package ru.nofeature.hackathon.data

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nofeature.hackathon.evaluate.impl.*


object SimpleCriteries : Table("criteries") {
    val name = varchar("name", 128)
    val description = varchar("description", 128)
    val maxScore = double("maxScore")
}

fun criteriesFromDb() : List<SimpleCriterion> = transaction {
    SimpleCriteries.selectAll().map { it ->
        SimpleCriterion(
            name = it[SimpleCriteries.name],
            description = it[SimpleCriteries.description],
            maxScore = it[SimpleCriteries.maxScore]
        )
    }
}

