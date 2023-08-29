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
import org.ktorm.entity.map
import org.slf4j.Logger

class ItemRoutes(application: Application) : KoinComponent {
    private val database: BpmDatabase by inject()
    private val logger: Logger by inject()

    @Serializable
    data class Item(
        val id: Int? = null,
        val name: String,
        val description: String,
    )

    init {
        with(application) {
            routing {
                authenticate {
                    getAllItems()
                    route("/item") {
                        putItem()
                        getItem()
                        modifyItem()
                        delItem()
                    }
                }
            }
        }
    }

    private fun Route.getAllItems() {
        get("/items") {
            val authenticated = bpmnAuth(
                routeInfo = "/items/",
                logger
            )
            if (!authenticated) {
                return@get
            }

            val data = database.itemDao.getItems().map {
                Item(it.id, it.name, it.description)
            }
            call.respond(data)
        }
    }

    private fun Route.putItem() {
        put {
            val authenticated = bpmnAuth(
                routeInfo = "/item/",
                logger
            )
            if (!authenticated) {
                return@put
            }
            val data = call.receive<Item>()
            val effectedRows = database.itemDao.insertItem(
                data.name,
                data.description
            )
            if (effectedRows == 1) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Something went wrong: $effectedRows effected rows!"
                )
            }
        }
    }

    private fun Route.getItem() {
        get("/{id}") {
            val authenticated = bpmnAuth(
                routeInfo = "/item/",
                logger
            )
            if (!authenticated) {
                return@get
            }
            if (call.parameters["id"] == null) {
                call.respond(
                    database.itemDao.getItems().map {
                        Item(it.id, it.name, it.description)
                    }
                )
            } else if (call.parameters["id"]?.toIntOrNull() != null) {
                val item = database.itemDao.getItemInfo(
                    call.parameters["id"]!!.toInt()
                )
                if (item == null) {
                    call.respond(
                        HttpStatusCode.NotFound,
                        message = "The item ${
                            call
                                .parameters["id"]
                        } could not be found!"
                    )
                } else {
                    call.respond(
                        Item(
                            item.id,
                            item.name,
                            item.description
                        )
                    )
                }
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    message = "Parameter was id was not null " +
                        "but was later on null. Invalid state!"
                )
            }
        }
    }

    private fun Route.modifyItem() {
        post {
            val authenticated = bpmnAuth(
                routeInfo = "/items/",
                logger
            )
            if (!authenticated) {
                return@post
            }
            val data = call.receive<Item>()

            val effectedRows = database.itemDao.modifyItem(
                data
            )
            if (effectedRows == 1) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Something went wrong: $effectedRows effected rows!"
                )
            }
        }
    }

    private fun Route.delItem() {
        delete("/{id}") {
            val authenticated = bpmnAuth(
                routeInfo = "/items/",
                logger
            )
            if (!authenticated) {
                return@delete
            }
            call.parameters["id"]?.toIntOrNull()?.let {
                database.itemDao.deleteItem(it)
                call.respond(HttpStatusCode.OK)
                return@delete
            }
            call.respond(
                HttpStatusCode.BadGateway,
                "Id should be an integer"
            )
        }
    }
}
