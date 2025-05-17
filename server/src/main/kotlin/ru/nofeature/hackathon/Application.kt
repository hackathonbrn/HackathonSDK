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
            val data: List<SimpleTeam> = transaction {
                SimpleTeams.selectAll().map {
                    SimpleTeam(
                        it[SimpleTeams.name],
                        it[SimpleTeams.command],
                        it[SimpleTeams.project],
                        it[SimpleTeams.role],
                    )
                }
            }
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
                val data: List<SimpleProjectRating> = transaction {
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
                val r = Json.encodeToString(data)
                call.respondText(r)
            }
        }
    }
}