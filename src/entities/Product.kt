package com.example.entities

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object ProductTable : UUIDTable("product") {
    val name = ProductTable.text("name")
    val price = ProductTable.double("price")
    val image = ProductTable.text("image")
    val productStock = ProductTable.bool("productStock")
}

class ProductEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ProductEntity>(ProductTable)
    var name by ProductTable.name
    var price by ProductTable.price
    var image by ProductTable.image
    var productStock by ProductTable.productStock
}
