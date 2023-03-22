package de.matthiasklenz.database.dao

import de.matthiasklenz.database.tables.UserDbTable
import de.matthiasklenz.database.tables.UserRecommendationsDbTable
import de.matthiasklenz.database.tables.UserType
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.dsl.update
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf

class UserDao(private val database: Database) {
    fun addUser(type: UserType = UserType.API, typeInfo: String? = null): Int {
        return database.insert(UserDbTable) {
            set(UserDbTable.type, type)
            if (typeInfo != null)
                set(UserDbTable.typeInfo, typeInfo)
        }
    }

    /**
     * adds or updates a user rating for an item
     * @param itemId the item the is rated
     * @param userid the user that is rating the item
     * @param value the value of the rating (1-5)
     */
    fun addRating(itemId: Int, userid: Int, value: Int): Int {
        val whereClause =
            (UserRecommendationsDbTable.itemId eq itemId) and (UserRecommendationsDbTable.userid eq userid)

        val entry = database
            .sequenceOf(UserRecommendationsDbTable)
            .find { whereClause }

        return if (entry != null) {
            database.update(UserRecommendationsDbTable) {
                where { whereClause }
                set(UserRecommendationsDbTable.rating, value)
            }
        } else {
            database.insert(UserRecommendationsDbTable) {
                set(UserRecommendationsDbTable.userid, userid)
                set(UserRecommendationsDbTable.itemId, itemId)
                set(UserRecommendationsDbTable.rating, value)
            }
        }
    }
}