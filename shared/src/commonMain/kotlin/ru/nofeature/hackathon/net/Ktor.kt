package ru.nofeature.hackathon.net

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import ru.nofeature.hackathon.Client
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
            Client.provide().post("$BASE_URL/addteams") {
                body = Json.encodeToString(team)
                contentType(ContentType.Text.Plain)
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
}