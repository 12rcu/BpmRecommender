package de.matthiasklenz.database.dao

import de.matthiasklenz.database.tables.UserDbTable
import de.matthiasklenz.database.tables.UserRatingDbEntity
import de.matthiasklenz.database.tables.UserRatingDbTable
import de.matthiasklenz.database.tables.UserType
import de.matthiasklenz.recommend.Recommender
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.dsl.update
import org.ktorm.entity.*

class UserDao(private val database: Database): KoinComponent {
    private val itemDao: ItemDao by inject()

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
            (UserRatingDbTable.itemId eq itemId) and (UserRatingDbTable.userid eq userid)

        val entry = database
            .sequenceOf(UserRatingDbTable)
            .find { whereClause }

        return if (entry != null) {
            database.update(UserRatingDbTable) {
                where { whereClause }
                set(UserRatingDbTable.rating, value)
            }
        } else {
            database.insert(UserRatingDbTable) {
                set(UserRatingDbTable.userid, userid)
                set(UserRatingDbTable.itemId, itemId)
                set(UserRatingDbTable.rating, value)
            }
        }
    }

    fun getRatings(userid: Int): EntitySequence<UserRatingDbEntity, UserRatingDbTable> {
        return database.sequenceOf(UserRatingDbTable).filter { it.userid eq userid }
    }

    fun getRatingsRecommender(userid: Int): Recommender.UserRating {
        return Recommender.UserRating(
            userid,
            getRatings(userid).associate {
                val itemName = itemDao.getItemInfo(it.itemId)?.name ?: "undefined"
                itemName to it.rating
            }
        )
    }

    fun getAllRatingsRecommender(): List<Recommender.UserRating> = database.sequenceOf(UserDbTable).map { user ->
        getRatingsRecommender(user.id)
    }
}