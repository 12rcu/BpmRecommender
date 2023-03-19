package de.matthiasklenz.database.tables

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.enum
import org.ktorm.schema.int
import org.ktorm.schema.text

interface UserDbEntity : Entity<UserDbEntity> {
    companion object : Entity.Factory<UserDbEntity>()

    val id: Int
    val type: UserType
    val typeInfo: String?
}

enum class UserType {
    GOOGLE,
    API
}

object UserDbTable : Table<UserDbEntity>("bpm_user") {
    val id = int("id").primaryKey().bindTo { it.id }
    val type = enum<UserType>("type").bindTo { it.type }
    val typeInfo = text("type_info").bindTo { it.typeInfo }
}