package ru.nofeature.hackathon

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import ru.nofeature.hackathon.data.RealmTeam
import ru.nofeature.hackathon.team.api.Team

object Database {
    val instanse: Realm by lazy {
        // use the RealmConfiguration.Builder() for more options
        val configuration = RealmConfiguration.create(schema = setOf(RealmTeam::class))
        val realm = Realm.open(configuration)
        realm
    }
}