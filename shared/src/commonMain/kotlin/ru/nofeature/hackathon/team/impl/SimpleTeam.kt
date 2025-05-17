package ru.nofeature.hackathon.team.impl

import kotlinx.serialization.Serializable
import ru.nofeature.hackathon.team.api.Team

@Serializable
data class SimpleTeam(
    override val name: String,
    override val command: String,
    override val project: String
) : Team