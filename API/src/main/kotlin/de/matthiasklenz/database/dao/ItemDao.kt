package de.matthiasklenz.database.dao

import de.matthiasklenz.database.tables.ItemCategoriesDbTable
import de.matthiasklenz.database.tables.ItemCategoryInheritanceDbTable
import de.matthiasklenz.database.tables.ItemDbTable
import org.ktorm.database.Database
import org.ktorm.dsl.*

class ItemDao(private val database: Database) {

    /**
     * @return for all items per item all categories as an own row as a [QuerySource]
     */
    private fun itemToCategorySource(): QuerySource {
        return database
            .from(ItemDbTable)
            .crossJoin(ItemCategoryInheritanceDbTable, on = ItemDbTable.id eq ItemCategoryInheritanceDbTable.itemId)
            .crossJoin(
                ItemCategoriesDbTable,
                on = ItemCategoryInheritanceDbTable.categoryId eq ItemCategoriesDbTable.id
            )
    }

    fun getItem(name: String): Query {
        return itemToCategorySource().select().where { ItemDbTable.name eq name }
    }

    fun insertItem(name: String, description: String): Int {
        return database.insert(ItemCategoriesDbTable) {
            set(ItemCategoriesDbTable.name, name)
            set(ItemCategoriesDbTable.description, description)
        }
    }
}