package de.matthiasklenz.plugins

import io.ktor.http.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.application.*

fun Application.configureHTTP() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.AccessControlAllowHeaders)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)

        //axios requests
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.Connection)
        allowHeader(HttpHeaders.AcceptLanguage)
        allowHeader(HttpHeaders.Host)
        allowHeader(HttpHeaders.Referrer)
        allowHeader(HttpHeaders.UserAgent)
        allowHeader(HttpHeaders.Accept)
        allowHeader(HttpHeaders.AcceptEncoding)
        allowHeader(HttpHeaders.Origin)
        allowHeader(HttpHeaders.Cookie)
        allowHeader(HttpHeaders.Upgrade)
        allowHeader("DNT")
        allowHeader("Sec-Fetch-Dest")
        allowHeader("Sec-Fetch-Mode")
        allowHeader("Sec-Fetch-Site")
        allowHeader("Sec-Fetch-User")
        allowHeader("Upgrade-Insecure-Requests")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }
}
