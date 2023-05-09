package de.matthiasklenz.routing

import de.matthiasklenz.database.BpmDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.ktorm.entity.map

class ItemRoutes(application: Application) : KoinComponent {
    private val database: BpmDatabase by inject()

    @Serializable
    data class Item(
        val id: Int? = null,
        val name: String,
        val description: String
    )

    init {
        with(application) {
            routing {
                allItems()
                item()
            }
        }
    }

    private fun Routing.allItems() {
        get("/items") {
            val data = database.itemDao.getItems().map {
                Item(it.id, it.name, it.description)
            }
            call.respond(data)
        }
    }

    private fun Routing.item() {
        route("/item") {
            put {
                val data = call.receive<Item>()
                val effectedRows = database.itemDao.insertItem(data.name, data.description)
                if (effectedRows == 1)
                    call.respond(HttpStatusCode.OK)
                else
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        "Something went wrong: $effectedRows effected rows!"
                    )
            }
            get("/{id}") {
                if (call.parameters["id"] == null) {
                    call.respond(database.itemDao.getItems().map { Item(it.id, it.name, it.description) })
                } else if (call.parameters["id"]?.toIntOrNull() != null) {
                    val query = database.itemDao.getItem(call.parameters["id"]!!.toInt())
                    if (query.totalRecords <= 0)
                        call.respond(HttpStatusCode.NotFound, "The item ${call.parameters["id"]} could not be found!")
                    else
                        call.respond(HttpStatusCode.NotFound, "The item ${call.parameters["id"]} could not be found!")
                } else {
                    call.respond(database.itemDao.getItem(call.parameters["id"]!!))
                }
            }
        }
    }
}