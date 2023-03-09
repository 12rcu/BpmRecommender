package de.matthiasklenz

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import de.matthiasklenz.plugins.*
import io.ktor.server.config.*

fun main(args: Array<String>) {
    val applicationEnvironment = commandLineEnvironment(args)
    NettyApplicationEngine(applicationEnvironment) {
        loadConfiguration(applicationEnvironment.config)
    }.start(true)
}

@Suppress("unused")     //set in application
fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureRouting()
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
