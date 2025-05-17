package ru.nofeature.hackathon.evaluate.impl

import kotlinx.serialization.Serializable
import ru.nofeature.hackathon.evaluate.api.Project

@Serializable
data class SimpleProject(
    override val name: String,
) : Project

@Serializable
data class ProjectWithTeam(
    override val name: String,
    val teamName: String
) : Project