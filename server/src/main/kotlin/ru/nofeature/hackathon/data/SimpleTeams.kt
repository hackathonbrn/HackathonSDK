package ru.nofeature.hackathon.data

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nofeature.hackathon.team.impl.SimpleTeam

object SimpleTeams : Table("simple_teams") {
    val name = varchar("name", 128)
    val command = varchar("command", 128)
    val project = varchar("project", 128)
    val role = varchar("role", 128)
}

fun teamFromDb() : List<SimpleTeam> = transaction {
    SimpleTeams.selectAll().map {
        SimpleTeam(
            it[SimpleTeams.name],
            it[SimpleTeams.command],
            it[SimpleTeams.project],
            it[SimpleTeams.role],
        )
    }
}