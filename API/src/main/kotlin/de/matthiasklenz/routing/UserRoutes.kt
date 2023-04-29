package de.matthiasklenz.routing

import de.matthiasklenz.database.BpmDatabase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.ktorm.entity.associate
import org.ktorm.entity.map

class UserRoutes(application: Application) : KoinComponent {
    private val database: BpmDatabase by inject()

    @Serializable
    data class UserRating(
        val userid: Int,
        val ratings: Map<Int, Int>
    )

    init {
        application.routing {
            authenticate {
                route("/user") {
                    getRatings()
                }
            }
        }
    }

    private fun Route.getRatings() {
        get("/ratings") {
            val data = database.userDao.getAllUsers().map { user ->
                UserRating(
                    user.id,
                    database.userDao.getRatings(user.id).associate { it.itemId to it.rating })
            }

            call.respond(data)
        }
    }
}