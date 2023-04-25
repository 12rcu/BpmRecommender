package de.matthiasklenz

import de.matthiasklenz.plugins.*
import de.matthiasklenz.routing.RecommenderRoutes
import io.ktor.server.application.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Server(application: Application): KoinComponent {
    private val jwtConfig: JwtConfig by inject()

    init {
        with(application) {
            configureSecurity(jwtConfig)
            configureHTTP()
            configureSerialization()
            configureRouting()

            RecommenderRoutes(this)
        }
    }
}