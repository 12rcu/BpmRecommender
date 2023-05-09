package de.matthiasklenz.database

import de.matthiasklenz.config.SqlDatabase
import de.matthiasklenz.database.dao.ItemDao
import de.matthiasklenz.database.dao.UserDao
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.qualifier
import org.ktorm.database.Database

class BpmDatabase: KoinComponent {
    private val dbProperties: SqlDatabase by inject(qualifier("main"))
    private val database: Database
    val itemDao: ItemDao
    val userDao: UserDao

    init {
        val dbUrl = "${dbProperties.host}:${dbProperties.port}"

        val jdbcUrl = "jdbc:mysql://$dbUrl/${dbProperties.schema}"
        val jdbcParameters = mapOf<String, Any>(
            "user" to dbProperties.user,
            "password" to dbProperties.password,
            "useSSL" to false,
            "allowPublicKeyRetrieval" to true,
            "zeroDateTimeBehavior" to "CONVERT_TO_NULL",
            "serverTimezone" to "Europe/Berlin"
        )

        database = Database.connect(
            "%s?%s".format(
                jdbcUrl,
                jdbcParameters.toList().joinToString("&") { (key, value) -> "$key=$value" }
            )
        )

        itemDao = ItemDao(database)
        userDao = UserDao(database)
    }
}