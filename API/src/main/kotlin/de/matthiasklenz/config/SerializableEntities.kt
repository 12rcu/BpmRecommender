package de.matthiasklenz.config

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val token: String,
    val databases: Databases,
    val auth: Auth
)

@Serializable
data class Databases (
    val main: SqlDatabase
)

@Serializable
data class SqlDatabase(
    val host: String,
    val schema: String,
    val user: String,
    val password: String
)

@Serializable
data class Auth(
    val googleSecret: String
)
