package de.matthiasklenz.database.dao

import de.matthiasklenz.database.tables.ItemCategoriesDbTable
import de.matthiasklenz.database.tables.ItemCategoryInheritanceDbTable
import de.matthiasklenz.database.tables.ItemDBEntity
import de.matthiasklenz.database.tables.ItemDbTable
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*

class ItemDao(private val database: Database) {

    /**
     * @return for all items per item all categories as an own row as a [QuerySource]
     */
    private fun itemToCategorySource(): QuerySource {
        return database
            .from(ItemDbTable)
            .crossJoin(
                ItemCategoryInheritanceDbTable,
                on = ItemDbTable.id eq ItemCategoryInheritanceDbTable.itemId
            )
            .crossJoin(
                ItemCategoriesDbTable,
                on = ItemCategoryInheritanceDbTable.categoryId eq ItemCategoriesDbTable.id
            )
    }

    fun getItem(name: String): Query {
        return itemToCategorySource().select().where {
            ItemDbTable.name eq name
        }
    }

    fun getItem(id: Int): Query {
        return itemToCategorySource().select().where {
            ItemDbTable.id eq id
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

    /**
     * add a new Category, use with caution as all items need updating
     *
     * @param name the name of the Category, should be unique
     * @param description optional description on the category
     * @param range the accepted range on the category, default 1 (true or false) but can also be higher to simulate for
     * example an effort range (unspecified, low, medium, high) => range = 3
     * @return the effected rows
     */
    fun addCategory(
        name: String,
        description: String = "",
        range: Int = 1,
    ): Int {
        return database.insert(ItemCategoriesDbTable) {
            set(ItemCategoriesDbTable.name, name)
            set(ItemCategoriesDbTable.description, description)
            set(ItemCategoriesDbTable.valueRange, range)
        }
    }

    /**
     * Insert or update a value of a given item & category, makes no attempt to verify if the value is possible and just
     * saves it into the database
     *
     * @param item the item id of the BPMN, note: item must already exist!
     * @param category the category id of items, note: category must already exist!
     * @return the affected rows
     */
    fun assessItemToCategory(
        item: Int,
        category: Int,
        value: Int,
    ): Int {
        val alreadyAssessed = database
            .sequenceOf(ItemCategoryInheritanceDbTable)
            .filter { (it.categoryId eq category) and (it.itemId eq item) }
            .firstOrNull() != null

        return if (alreadyAssessed) {
            // update the entry
            database.update(ItemCategoryInheritanceDbTable) {
                where {
                    (ItemCategoryInheritanceDbTable.categoryId eq category) and (ItemCategoryInheritanceDbTable.itemId eq item)
                }
                set(ItemCategoryInheritanceDbTable.value, value)
            }
        } else {
            // insert a new entry
            database.insert(ItemCategoryInheritanceDbTable) {
                set(ItemCategoryInheritanceDbTable.itemId, item)
                set(
                    ItemCategoryInheritanceDbTable.categoryId,
                    category
                )
                set(ItemCategoryInheritanceDbTable.value, value)
            }
        }
    }
}
