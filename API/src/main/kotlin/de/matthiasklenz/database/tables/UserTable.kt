package de.matthiasklenz.database.tables

import org.ktorm.entity.Entity
import org.ktorm.schema.*

interface UserDbEntity : Entity<UserDbEntity> {
    companion object : Entity.Factory<UserDbEntity>()

    val id: Int
    val name: String
    val info: String?
}

object UserDbTable : Table<UserDbEntity>("bpm_user") {
    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val info = text("info").bindTo { it.info }
}