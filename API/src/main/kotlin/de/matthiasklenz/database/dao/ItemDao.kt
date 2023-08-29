package de.matthiasklenz.database.dao

import de.matthiasklenz.database.tables.ItemDBEntity
import de.matthiasklenz.database.tables.ItemDbTable
import de.matthiasklenz.routing.ItemRoutes
import org.ktorm.database.Database
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.dsl.update
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf

class ItemDao(private val database: Database) {
    fun modifyItem(item: ItemRoutes.Item): Int {
        return database.update(ItemDbTable) {
            set(it.name, item.name)
            set(it.description, item.description)

            where {
                it.id eq item.id!!
            }
        }
    }

    fun getItems(): EntitySequence<ItemDBEntity, ItemDbTable> {
        return database.sequenceOf(ItemDbTable)
    }

    fun getItemInfo(id: Int): ItemDBEntity? {
        return database.sequenceOf(
            ItemDbTable
        ).find { it.id eq id }
    }

    /**
     * must ensure that categories are set as well, if a category isn't set we assume the item doesn't have this
     * characteristic
     *
     * @param name the name of the item/BPMN
     * @param description an optional description of the item/BPMN
     * @return the effected rows
     */
    fun insertItem(name: String, description: String = ""): Int {
        return database.insert(ItemDbTable) {
            set(ItemDbTable.name, name)
            set(ItemDbTable.description, description)
        }
    }

    fun deleteItem(id: Int) {
        database.delete(ItemDbTable) {
            it.id eq id
        }
    }
}
