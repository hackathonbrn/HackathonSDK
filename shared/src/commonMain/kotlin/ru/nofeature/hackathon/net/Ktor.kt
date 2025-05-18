package ru.nofeature.hackathon.net

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import ru.nofeature.hackathon.Client
import ru.nofeature.hackathon.ProjectSummary
import ru.nofeature.hackathon.evaluate.impl.SimpleCriterion
import ru.nofeature.hackathon.evaluate.impl.SimpleProjectRating
import ru.nofeature.hackathon.team.impl.SimpleTeam

object Ktor {
    private val coroutineScopen = CoroutineScope(SupervisorJob())
    private const val BASE_URL = "http://192.168.31.57:7777"
    private val json: Json by lazy {
        Json { ignoreUnknownKeys = true }
    }

    @OptIn(InternalAPI::class)
    fun addTeammate(
        team: SimpleTeam
    ) {
        coroutineScopen.launch {
            Client.provide().use {
                it.post("$BASE_URL/addteams") {
                    body = Json.encodeToString(team)
                    contentType(ContentType.Text.Plain)
                }
            }
        }
    }

    suspend fun loadTeams(): List<SimpleTeam> {
        val bodyAsString = Client.provide().get(
            "$BASE_URL/teams"
        ).body<String>()
        return json.decodeFromString(bodyAsString)
    }

    @OptIn(InternalAPI::class)
    fun addEvaluate(projectRating: SimpleProjectRating) {
        coroutineScopen.launch {
            Client.provide().post("$BASE_URL/evaluate/score") {
                body = Json.encodeToString(projectRating)
                contentType(ContentType.Text.Plain)
            }
        }
    }

    suspend fun loadReport() : List<ProjectSummary> {
        val bodyAsString = Client.provide().get(
            "$BASE_URL/report"
        ).body<String>()
        return json.decodeFromString(bodyAsString)
    }

    suspend fun loadCriterion() : List<SimpleCriterion> {
        val bodyAsString = Client.provide().get(
            "$BASE_URL/criteries"
        ).body<String>()
        return json.decodeFromString(bodyAsString)
    }

    @OptIn(InternalAPI::class)
    fun createCriterion(criterion: SimpleCriterion) {
        coroutineScopen.launch {
            Client.provide().post("$BASE_URL/criteries") {
                body = Json.encodeToString(criterion)
                contentType(ContentType.Text.Plain)
            }
        }
    }
    @OptIn(InternalAPI::class)
    fun deleteCriterion(criterion: SimpleCriterion) {
        coroutineScopen.launch {
            Client.provide().delete("$BASE_URL/criteries") {
                body = Json.encodeToString(criterion)
                contentType(ContentType.Text.Plain)
            }
        }
    }
}