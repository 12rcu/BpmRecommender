package de.matthiasklenz

import de.matthiasklenz.config.ConfigLoader
import de.matthiasklenz.database.BpmDatabase
import de.matthiasklenz.plugins.JwtConfig
import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.loadCommonConfiguration
import io.ktor.server.netty.NettyApplicationEngine
import org.koin.core.qualifier.qualifier
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    val applicationEnvironment = commandLineEnvironment(args)
    NettyApplicationEngine(applicationEnvironment) {
        loadConfiguration(applicationEnvironment.config)
    }.start(true)
}

@Suppress("unused") // set in application
fun Application.module() {
    val config = ConfigLoader().loadConfig().getOrElse {
        log.error(it.message)
        this.dispose()
        return
    }

    val logger = LoggerFactory.getLogger("api")

    val appModules = org.koin.dsl.module {
        single { config }
        single(qualifier("main")) { config.databases.main }
        single { logger }
        single { BpmDatabase() }
        single { JwtConfig(config.token) }
    }

    Server(this, appModules)
}

private fun NettyApplicationEngine.Configuration.loadConfiguration(config: ApplicationConfig) {
    val deploymentConfig = config.config("ktor.deployment")
    loadCommonConfiguration(deploymentConfig)
    deploymentConfig.propertyOrNull(
        "requestQueueLimit"
    )?.getString()?.toInt()?.let {
        requestQueueLimit = it
    }
    deploymentConfig.propertyOrNull(
        "shareWorkGroup"
    )?.getString()?.toBoolean()?.let {
        shareWorkGroup = it
    }
    deploymentConfig.propertyOrNull(
        "responseWriteTimeoutSeconds"
    )?.getString()?.toInt()?.let {
        responseWriteTimeoutSeconds = it
    }
}
