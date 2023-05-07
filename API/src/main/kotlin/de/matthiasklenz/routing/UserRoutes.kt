package de.matthiasklenz.routing

import de.matthiasklenz.database.BpmDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
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

    @Serializable
    data class UserData(
        val userid: Int?,
        val username: String,
        val info: String?
    )

    init {
        application.routing {
            authenticate {
                route("/users") {
                    getUsers()
                }
                route("/user") {
                    getRatings()
                    createUser()
                    getUser()
                    route("/rating") {
                        addRating()
                    }
                }
            }
        }
    }

    private fun Route.getUsers() {
        get {
            call.respond(database.userDao.getAllUsers().map { UserData(it.id, it.name, it.info) })
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

    private fun Route.createUser() {
        put {
            val data = call.receive<UserData>()
            val changes = database.userDao.addUser(data.username, data.info)
            if (changes == 0)
                call.respond(HttpStatusCode.InternalServerError, "Something went wrong, nothing has changed!")
            else
                call.respond(HttpStatusCode.OK)
        }
    }

    private fun Route.getUser() {
        get("{id}") {
            val userid = call.parameters["userid"]?.toInt() ?: 0
            if (userid <= 0) {
                call.respond(HttpStatusCode.BadRequest, "Invalid userid '$userid'")
            }
            val data = database.userDao.getUser(userid)
            call.respond(UserData(data?.id, data?.name ?: "undefined", data?.info))
        }
    }

    private fun Route.addRating() {
        post {
            val data = call.receive<UserRating>()
            data.ratings.forEach { (item, rating) ->
                database.userDao.addRating(item, data.userid, rating)
            }
        }
    }
}