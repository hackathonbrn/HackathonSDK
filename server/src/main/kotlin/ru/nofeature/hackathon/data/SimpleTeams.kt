package ru.nofeature.hackathon.data

import org.jetbrains.exposed.sql.Table

object SimpleTeams : Table("simple_teams") {
    val name = varchar("name", 128)
    val command = varchar("command", 128)
    val project = varchar("project", 128)
}