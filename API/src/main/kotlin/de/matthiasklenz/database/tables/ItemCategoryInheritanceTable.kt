package de.matthiasklenz.database.tables

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int

interface ItemCategoryInheritanceDBEntity: Entity<ItemCategoryInheritanceDBEntity> {
    companion object: Entity.Factory<ItemCategoryInheritanceDBEntity>()

    val id: Int
    val itemId: Int
    val categoryId: Int
}

object ItemCategoryInheritanceDbTable: Table<ItemCategoryInheritanceDBEntity>("bpm_item_category_inheritance") {
    val id = int("id").primaryKey().bindTo { it.id }
    val itemId = int("item_id").bindTo { it.itemId }
    val categoryId = int("category_id").bindTo { it.categoryId }
}