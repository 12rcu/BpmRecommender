package de.matthiasklenz.config

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val token: String,
    val databases: Databases,
    val allowCORS: Boolean = false,
    val allowedCORS: List<String> = listOf(),
)

@Serializable
data class Databases(
    val main: SqlDatabase,
)

@Serializable
data class SqlDatabase(
    val host: String,
    val port: Int,
    val schema: String,
    val user: String,
    val password: String,
)
