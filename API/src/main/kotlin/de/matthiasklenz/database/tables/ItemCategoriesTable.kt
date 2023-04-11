package de.matthiasklenz.database.tables

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.text
import org.ktorm.schema.varchar

interface ItemCategoriesDBEntity: Entity<ItemCategoriesDBEntity> {
    companion object: Entity.Factory<ItemCategoriesDBEntity>()

    val id: Int
    val name: String
    val description: String
    val valueRange: Int
}

object ItemCategoriesDbTable: Table<ItemCategoriesDBEntity>("bpm_item_categories") {
    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val description = text("description").bindTo { it.description }
    val valueRange = int("value_range").bindTo { it.valueRange }
}