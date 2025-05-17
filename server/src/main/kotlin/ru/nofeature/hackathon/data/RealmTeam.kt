package ru.nofeature.hackathon.data

import io.realm.kotlin.types.RealmObject
import kotlinx.serialization.Serializable
import ru.nofeature.hackathon.team.api.Team

@Serializable
data class RealmTeam(
    override val name: String
) : Team, RealmObject
