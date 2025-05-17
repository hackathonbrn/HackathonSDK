package ru.nofeature.hackathon.data

import org.jetbrains.exposed.sql.Table

object SimpleTeams : Table("simple_teams") {
    val name = varchar("name", 128)
    val command = varchar("command", 128)
    val project = varchar("project", 128)
    val role = varchar("role", 128)
}

object SimpleEvaluations : Table("evaluations") {
    val jujde = varchar("judge", 128)
    val project = varchar("project", 128)
    val score = float("score")
    val criteria = varchar("criteria", 128)
}