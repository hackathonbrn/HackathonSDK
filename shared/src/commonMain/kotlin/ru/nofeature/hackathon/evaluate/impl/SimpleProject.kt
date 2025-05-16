package ru.nofeature.hackathon.evaluate.impl

import kotlinx.serialization.Serializable
import ru.nofeature.hackathon.evaluate.api.Project

@Serializable
data class SimpleProject(
    override val id: String,
    override val name: String,
) : Project