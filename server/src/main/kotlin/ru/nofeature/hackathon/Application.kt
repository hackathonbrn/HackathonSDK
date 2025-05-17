package ru.nofeature.hackathon

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.realm.kotlin.ext.query
import ru.nofeature.hackathon.data.RealmTeam

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    routing {
        route("/teams") {
            get {
                val data = Database.instanse.query<RealmTeam>().find()
                call.respond(data)
            }
            post {
                val data = call.receive<RealmTeam>()
                Database.instanse.writeBlocking {
                    copyToRealm(data)
                }
                call.respondText("nice")
            }
        }
    }
}