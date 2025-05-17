package ru.nofeature.hackathon

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nofeature.hackathon.data.SimpleTeams
import ru.nofeature.hackathon.team.impl.SimpleTeam

fun main() {
    val database = Database.connect(
        "jdbc:sqlite:identifier.sqlite",
        "org.sqlite.JDBC",
    )
    transaction(database) {
        SchemaUtils.create(SimpleTeams)
    }

    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0") {
        module()
        install(ContentNegotiation) {
            Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            }
        }
    }
        .start(wait = true)
}

fun Application.module() {
    routing {
        route("/teams") {
            get {
                val data: List<SimpleTeam> = transaction {
                    SimpleTeams.selectAll().map {
                        SimpleTeam(
                            it[SimpleTeams.name],
                            it[SimpleTeams.command],
                            it[SimpleTeams.project]
                        )
                    }
                }
                val r = Json.encodeToString(data)
                call.respondText(r)
            }
            post {
                val data = call.receive<SimpleTeam>()
                transaction {
                    SimpleTeams.insert {
                        it[name] = data.name
                        it[command] = data.command
                        it[project] = data.project
                    }
                }
                call.respondText("nice")
            }
        }
    }
}