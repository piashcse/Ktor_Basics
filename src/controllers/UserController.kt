package com.example.controllers

import com.example.entities.UserEntity
import com.example.entities.UserTable
import com.example.models.User
import entities.FruitEntity
import models.Fruit
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UserController {
    fun all() = transaction {
        return@transaction UserEntity.all().map {
            User(it.mobile, it.name, it.password)
        }.toList()
    }

    fun find(user: User) = transaction {
        val userEntity = UserEntity.find{UserTable.mobile eq user.mobile}.toList().singleOrNull()
        return@transaction if (userEntity != null) {
            User(userEntity.mobile, userEntity.name, userEntity.password)
        }else null
    }
    fun findByMobile(mobile: String) = transaction {
        val userEntity = UserEntity.find{UserTable.mobile eq mobile}.toList().singleOrNull()
        return@transaction if (userEntity != null) {
            User(userEntity.mobile, userEntity.name, userEntity.password)
        }else null
    }

    fun add(user: User) = transaction {
        var userEntity = UserEntity.find { UserTable.mobile eq user.mobile }.toList().singleOrNull()
        return@transaction if (userEntity == null) {
            userEntity = UserEntity.new {
                mobile = user.mobile
                name = user.name
                password = user.password
            }
            User(userEntity.mobile, userEntity.name, userEntity.password)
        } else null
    }

    fun edit(id: Int, fruit: Fruit) = transaction {
        val fruitEntity = FruitEntity.findById(id)

        return@transaction if (fruitEntity != null) {
            fruitEntity.description = fruit.description

            Fruit(fruitEntity.id.value, fruitEntity.no, fruitEntity.description)
        } else null
    }

    fun delete(id: Int) = transaction {
        val fruitEntity = FruitEntity.findById(id)

        return@transaction if (fruitEntity != null) {
            fruitEntity.delete()

            Fruit(fruitEntity.id.value, fruitEntity.no, fruitEntity.description)
        } else null
    }
}
