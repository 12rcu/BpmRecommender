package de.matthiasklenz.plugins

import de.matthiasklenz.config.ConfigLoader
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import org.slf4j.Logger

fun Application.configureSecurity(jwtConfig: JwtConfig) {
    authentication {
        jwt {
            jwtConfig.configureKtorFeature(this)
        }
    }
}

suspend fun PipelineContext<Unit, ApplicationCall>.bpmnAuth(routeInfo: String, logger: Logger): Boolean {
    val principal = call.principal<JwtConfig.User>()
    return if(principal?.role == "admin") {
        logger.info("Successfully authenticated '${principal.userinfo}' to access '$routeInfo'!")
        true
    } else {
        if(principal == null) {
            call.respond(HttpStatusCode.Unauthorized, "Please reauthenticate!")
        } else {
            call.respond(HttpStatusCode.Forbidden, "You don't have the correct role to access this Route!")
        }
        logger.warn("Denied access for '$principal' to access '$routeInfo'!")
        false
    }
}

fun main() {
    val config = ConfigLoader().loadConfig()
    val token = JwtConfig(config.token).generateToken(JwtConfig.User("matthias", "admin"))
    println(token)
}
