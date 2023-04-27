package de.matthiasklenz.routing

import de.matthiasklenz.database.BpmDatabase
import de.matthiasklenz.database.dao.ItemDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.ktorm.dsl.forEach
import org.ktorm.dsl.limit
import org.ktorm.entity.map

class ItemRoutes(application: Application) : KoinComponent {
    private val database: BpmDatabase by inject()

    @Serializable
    data class Item(
        val name: String,
        val description: String,
        val id: Int
    )

    init {
        with(application) {
            routing {
                allItems()
                createItem()
            }
        }
    }

    private fun Routing.allItems() {
        get("/items") {
            val data = database.itemDao.getItems().map {
                Item(it.name, it.description, it.id)
            }
            call.respond(data)
        }
    }

    private fun Routing.createItem() {
        route("/item/{id}") {
            put {

            }
            post {

            }
            get {
                if (call.parameters["id"] == null) {
                    call.respond(database.itemDao.getItems().map { SerializedItem(it.id, it.name, it.description) })
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

    @Serializable
    data class SerializedItem(
        val id: Int,
        val name: String,
        val description: String
    )
}