package de.matthiasklenz.database.tables

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int

interface UserRatingDbEntity : Entity<UserRatingDbEntity> {
    companion object : Entity.Factory<UserDbEntity>()

    val id: Int
    val userid: Int
    val itemId: Int
    val rating: Int
}

object UserRatingDbTable : Table<UserRatingDbEntity>(
    "bpm_user_recommendation"
) {
    val id = int("id").primaryKey().bindTo { it.id }
    val userid = int("userid").bindTo { it.userid }
    val itemId = int("item_id").bindTo { it.itemId }
    val rating = int("rating").bindTo { it.rating }
}
