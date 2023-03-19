package de.matthiasklenz.database.tables

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int

interface UserRecommendationDbEntity: Entity<UserRecommendationDbEntity> {
    companion object: Entity.Factory<UserDbEntity>()

    val id: Int
    val userid: Int
    val itemId: Int
}

object UserRecommendationsDbTable: Table<UserRecommendationDbEntity>("bpm_user_recommendation") {
    val id = int("id").primaryKey().bindTo { it.id }
    val userid = int("userid").bindTo { it.userid }
    val itemId = int("item_id").bindTo { it.itemId }
}