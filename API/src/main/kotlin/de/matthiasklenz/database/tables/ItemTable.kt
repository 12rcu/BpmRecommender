package de.matthiasklenz.database.tables

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.text
import org.ktorm.schema.varchar

interface ItemDBEntity: Entity<ItemDBEntity> {
    companion object: Entity.Factory<ItemDBEntity>()

    val id: Int
    val name: String
    val description: String
}

object ItemDbTable: Table<ItemDBEntity>("bpm_items") {
    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val description = text("description").bindTo { it.description }
}