package de.matthiasklenz

import de.matthiasklenz.plugins.configureHTTP
import de.matthiasklenz.plugins.configureRouting
import de.matthiasklenz.plugins.configureSecurity
import de.matthiasklenz.plugins.configureSerialization
import io.ktor.server.application.*

class Server(application: Application) {
    init {
        with(application) {
            configureSecurity()
            configureHTTP()
            configureSerialization()
            configureRouting()
        }
    }
}