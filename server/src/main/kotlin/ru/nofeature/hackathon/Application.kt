package ru.nofeature.hackathon

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nofeature.hackathon.data.SimpleEvaluations
import ru.nofeature.hackathon.data.SimpleTeams
import ru.nofeature.hackathon.data.evaluateFromDb
import ru.nofeature.hackathon.data.teamFromDb
import ru.nofeature.hackathon.evaluate.impl.*
import ru.nofeature.hackathon.team.impl.SimpleTeam

fun main() {
    val database = Database.connect(
        "jdbc:sqlite:identifier.sqlite",
        "org.sqlite.JDBC",
    )
    transaction(database) {
        SchemaUtils.create(SimpleTeams,SimpleEvaluations)
    }

    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0") {
        install(ContentNegotiation) {
            Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            }
        }
        install(CORS) {
            anyHost()
        }
        module()
    }
        .start(wait = true)
}

fun Application.module() {
    routing {
        get("/teams") {
            val data: List<SimpleTeam> = teamFromDb()
            val r = Json.encodeToString(data)
            call.respondText(r)
        }
        post("addteams") {
            val data = call.receiveText()
            val team = Json.decodeFromString<SimpleTeam>(data)
            transaction {
                SimpleTeams.insert {
                    it[name] = team.name
                    it[command] = team.command
                    it[project] = team.project
                    it[role] = team.role
                }
            }
            call.respondText("nice")
        }

        get("report"){
            val jsData = Json.encodeToString(generateReport())
            call.respondText(jsData)
        }

        route("evaluate") {
            post("score") {
                val data = call.receiveText()
                val projectRating = Json.decodeFromString<SimpleProjectRating>(data)
                transaction {
                    projectRating.ratings.forEach { rating ->
                        SimpleEvaluations.insert {
                            it[project] = projectRating.project.name
                            it[jujde] = projectRating.judge.name
                            it[criteria] = rating.criterion.name
                            it[score] = rating.score.toFloat()
                        }
                    }
                }
                call.respondText("nice")
            }
            get {
                val data: List<SimpleProjectRating> = evaluateFromDb()
                val r = Json.encodeToString(data)
                call.respondText(r)
            }
        }
    }
}
