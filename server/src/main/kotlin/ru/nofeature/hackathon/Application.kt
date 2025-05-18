package ru.nofeature.hackathon

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.nofeature.hackathon.data.*
import ru.nofeature.hackathon.evaluate.impl.*
import ru.nofeature.hackathon.net.Ktor.loadCriterion
import ru.nofeature.hackathon.team.impl.SimpleTeam

fun main() {
    val database = Database.connect(
        "jdbc:sqlite:identifier.sqlite",
        "org.sqlite.JDBC",
    )
    transaction(database) {
        SchemaUtils.create(SimpleTeams, SimpleEvaluations, SimpleCriteries)
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

        get("report") {
            val jsData = Json.encodeToString(generateReport())
            call.respondText(jsData)
        }
        route("criteries") {
            get {
                val data: List<SimpleCriterion> = criteriesFromDb()
                val r = Json.encodeToString(data)
                call.respondText(r)
            }
            post {
                val data = call.receiveText()
                val criterion = Json.decodeFromString<SimpleCriterion>(data)
                transaction {
                    SimpleCriteries.insert {
                        it[name] = criterion.name
                        it[description] = criterion.description
                        it[maxScore] = criterion.maxScore
                    }
                }
                call.respondText(status = HttpStatusCode.OK, text = "nice")
            }
            delete {
                val data = call.receiveText()
                val criterion = Json.decodeFromString<SimpleCriterion>(data)
                transaction {
                    SimpleCriteries.deleteWhere { SimpleCriteries.name eq criterion.name }
                }
                call.respondText(status = HttpStatusCode.OK, text = "nice")
            }
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
                call.respondText(status = HttpStatusCode.OK, text = "nice")
            }
            get {
                val data: List<SimpleProjectRating> = evaluateFromDb()
                val r = Json.encodeToString(data)
                call.respondText(status = HttpStatusCode.OK, text = r)
            }
        }
    }
}
