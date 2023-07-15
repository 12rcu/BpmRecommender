package de.matthiasklenz.plugins

import de.matthiasklenz.config.ConfigLoader
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.util.pipeline.PipelineContext
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
    return if (principal?.role == "admin") {
        logger.info(
            "Successfully authenticated '${principal.userinfo}' to access '$routeInfo'!"
        )
        true
    } else {
        if (principal == null) {
            call.respond(
                HttpStatusCode.Unauthorized,
                "Please reauthenticate!"
            )
        } else {
            call.respond(
                HttpStatusCode.Forbidden,
                "You don't have the correct role to access this Route!"
            )
        }
        logger.warn(
            "Denied access for '$principal' to access '$routeInfo'!"
        )
        false
    }
}

fun main() {
    val config = ConfigLoader().loadConfig()!!  //for testing
    val token = JwtConfig(
        config.token
    ).generateToken(JwtConfig.User("matthias", "admin"))
    println(token)
}
