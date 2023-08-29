package de.matthiasklenz.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.core.module.Module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin(vararg appModules: Module) {
    install(Koin) {
        slf4jLogger()
        modules(appModules.toList())
    }
}
