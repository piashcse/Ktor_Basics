package com.example.entities

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import java.util.*

object UserTable : IntIdTable("user") {
    val mobile = UserTable.text("mobile").uniqueIndex()
    val name = UserTable.text("name")
    val password = UserTable.text("password")
    override val primaryKey = PrimaryKey(mobile)

}

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UserTable)

    var mobile by UserTable.mobile
    var name by UserTable.name
    var password by UserTable.password
}
