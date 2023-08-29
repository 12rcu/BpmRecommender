package de.matthiasklenz

import de.matthiasklenz.config.Config
import de.matthiasklenz.plugins.*
import de.matthiasklenz.routing.ItemRoutes
import de.matthiasklenz.routing.RecommenderRoutes
import de.matthiasklenz.routing.UserRoutes
import io.ktor.server.application.Application
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.Module

class Server(application: Application, appModules: Module) :
    KoinComponent {
    private val jwtConfig: JwtConfig by inject()
    private val config: Config by inject()

    init {
        with(application) {
            configureKoin(
                appModules,
                RecommenderRoutes.koinModule()
            )
            configureSecurity(jwtConfig)
            configureHTTP(config)
            configureSerialization()
            configureRouting()

            RecommenderRoutes(this)
            ItemRoutes(this)
            UserRoutes(this)
        }
    }
}
