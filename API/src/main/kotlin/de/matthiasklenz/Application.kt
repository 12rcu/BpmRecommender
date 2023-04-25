package de.matthiasklenz

import de.matthiasklenz.config.ConfigLoader
import de.matthiasklenz.database.BpmDatabase
import de.matthiasklenz.plugins.JwtConfig
import de.matthiasklenz.routing.RecommenderRoutes
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.context.startKoin
import org.koin.core.qualifier.qualifier
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    val applicationEnvironment = commandLineEnvironment(args)
    NettyApplicationEngine(applicationEnvironment) {
        loadConfiguration(applicationEnvironment.config)
    }.start(true)
}

@Suppress("unused")     //set in application
fun Application.module() {
    val config = ConfigLoader().loadConfig()
    val logger = LoggerFactory.getLogger("api")

    startKoin {
        modules(
            org.koin.dsl.module {
                single { config }
                single(qualifier("main")) { config.databases.main }
                single { logger }
                single { BpmDatabase() }
                single { JwtConfig(config.token) }
            },
            RecommenderRoutes.koinModule()
        )
    }

    Server(this)
}

private fun NettyApplicationEngine.Configuration.loadConfiguration(config: ApplicationConfig) {
    val deploymentConfig = config.config("ktor.deployment")
    loadCommonConfiguration(deploymentConfig)
    deploymentConfig.propertyOrNull("requestQueueLimit")?.getString()?.toInt()?.let {
        requestQueueLimit = it
    }
    deploymentConfig.propertyOrNull("shareWorkGroup")?.getString()?.toBoolean()?.let {
        shareWorkGroup = it
    }
    deploymentConfig.propertyOrNull("responseWriteTimeoutSeconds")?.getString()?.toInt()?.let {
        responseWriteTimeoutSeconds = it
    }
}
