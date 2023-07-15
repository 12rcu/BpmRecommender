package de.matthiasklenz.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.Principal
import io.ktor.server.auth.jwt.JWTAuthenticationProvider

class JwtConfig(jwtSecret: String) {
    companion object Constants {
        private const val CLAIM_USERINFO = "userinfo"
        private const val CLAIM_USER_ROLE = "userRole"
        private const val jwtIssuer = "de.matthiasklenz.upload"
        private const val jwtRealm = "de.matthiasklenz.upload"
    }

    private val jwtAlgorithm = Algorithm.HMAC512(jwtSecret)
    private val jwtVerifier: JWTVerifier = JWT
        .require(jwtAlgorithm)
        .withIssuer(jwtIssuer)
        .build()

    /**
     * Generate a token for an authenticated user
     */
    fun generateToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(jwtIssuer)
        .withClaim(CLAIM_USERINFO, user.userinfo)
        .withClaim(CLAIM_USER_ROLE, user.role)
        .sign(jwtAlgorithm)

    /**
     * Configure the jwt ktor authentication feature
     */
    fun configureKtorFeature(
        config: JWTAuthenticationProvider.Config,
    ) = with(config) {
        verifier(jwtVerifier)
        realm = jwtRealm
        validate {
            val userinfo = it.payload.getClaim(
                CLAIM_USERINFO
            ).asString()
            val userRole = it.payload.getClaim(
                CLAIM_USER_ROLE
            ).asString()

            if (userinfo != null && userRole != null) {
                User(userinfo, userRole)
            } else {
                null
            }
        }
    }

    /**
     * data object, that contains information of a user that is authenticated via jwt
     */
    data class User(
        val userinfo: String,
        val role: String,
    ) : Principal
}
