package de.matthiasklenz.routing

import de.matthiasklenz.database.BpmDatabase
import de.matthiasklenz.plugins.bpmnAuth
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.ktorm.entity.associate
import org.ktorm.entity.map
import org.slf4j.Logger

class UserRoutes(application: Application) : KoinComponent {
    private val database: BpmDatabase by inject()
    private val logger: Logger by inject()

    @Serializable
    data class UserRating(
        val userid: Int,
        val ratings: Map<String, Int>,
    )

    @Serializable
    data class UserData(
        val userid: Int? = null,
        val username: String,
        val info: String? = null,
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
            val authenticated = bpmnAuth(
                routeInfo = "/users/",
                logger
            )
            if (!authenticated) {
                return@get
            }

            call.respond(
                database.userDao.getAllUsers().map {
                    UserData(it.id, it.name, it.info)
                }
            )
        }
    }

    private fun Route.getRatings() {
        get("/ratings") {
            val authenticated = bpmnAuth(
                routeInfo = "/user/ratings/",
                logger
            )
            if (!authenticated) {
                return@get
            }


            val data =
                database.userDao.getAllUsers().map { user ->
                    UserRating(
                        user.id,
                        database.userDao.getRatings(
                            user.id
                        ).associate {
                            it.itemId.toString() to it.rating
                        }
                    )
                }

            call.respond(data)
        }
    }

    private fun Route.createUser() {
        put {
            val authenticated = bpmnAuth(
                routeInfo = "/user/",
                logger
            )
            if (!authenticated) {
                return@put
            }

            val data = call.receive<UserData>()
            val changes = database.userDao.addUser(
                data.username,
                data.info
            )
            if (changes == 0) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Something went wrong, nothing has changed!"
                )
            } else {
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    private fun Route.delUser() {
        delete("{id}") {
            val authenticated = bpmnAuth(
                routeInfo = "/user/",
                logger
            )
            if (!authenticated) {
                return@delete
            }

            call.parameters["id"]?.toIntOrNull()?.let {
                database.userDao.delUser(it)
                call.respond(HttpStatusCode.OK)
                return@delete
            }
            call.respond(
                HttpStatusCode.BadRequest,
                "Id should be an integer!"
            )
        }
    }

    private fun Route.getUser() {
        get("{id}") {
            val authenticated = bpmnAuth(
                routeInfo = "/user/{id}",
                logger
            )
            if (!authenticated) {
                return@get
            }

            val userid =
                call.parameters["id"]?.toIntOrNull() ?: 0
            if (userid <= 0) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Invalid userid '$userid'"
                )
            }
            val data = database.userDao.getUser(userid)
            call.respond(
                UserData(
                    data?.id,
                    data?.name ?: "undefined",
                    data?.info
                )
            )
        }
    }

    private fun Route.addRating() {
        post {
            val authenticated = bpmnAuth(
                routeInfo = "/user/rating/",
                logger
            )
            if (!authenticated) {
                return@post
            }

            val data = call.receive<UserRating>()
            data.ratings.forEach { (item, rating) ->
                database.userDao.addRating(
                    item.toInt(),
                    data.userid,
                    rating.coerceIn(1..5)
                )
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}
