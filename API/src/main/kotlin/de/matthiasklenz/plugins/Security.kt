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

suspend fun PipelineContext<Unit, ApplicationCall>.bpmnAuth(
    routeInfo: String,
    logger: Logger,
): Boolean {
    val principal = call.principal<JwtConfig.User>()
    val hasAccess: Boolean = (principal?.role == "admin")
    val noToken: Boolean = principal == null

    val success = "Successfully authenticated " +
        "'${principal?.userinfo}' to access '$routeInfo'!"
    val denied = "Denied access for '$principal' to access " +
        "'$routeInfo'!"

    val unAuthMsg = "Please reauthenticate!"
    val forbidden =
        "You don't have the correct role to access " +
            "this Route!"

    return if (hasAccess) {
        logger.info(success)
        true
    } else if (noToken) {
        logger.warn(denied)
        call.respond(HttpStatusCode.Unauthorized, unAuthMsg)
        false
    } else {
        logger.warn(denied)
        call.respond(HttpStatusCode.Forbidden, forbidden)
        false
    }
}

fun main() {
    val config = ConfigLoader().loadConfig().getOrElse {
        println(it.message)
        return
    }
    val token = JwtConfig(
        config.token
    ).generateToken(JwtConfig.User("matthias", "admin"))
    println(token)
}
