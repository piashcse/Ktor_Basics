package com.example.controllers

import com.example.entities.ProductEntity
import com.example.entities.ProductTable
import com.example.models.Product
import entities.FruitEntity
import entities.Fruits
import models.Fruit
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class ProductController {
    fun add(product: Product) = transaction {
             val productEntity = ProductEntity.new {
                name = product.name
                price = product.price
                image = product.image
                productStock = product.productStock
            }
        return@transaction if (productEntity != null){
            Product(productEntity.id.toString(),productEntity.name, productEntity.price, productEntity.image, productEntity.productStock)
        }else null
    }
    fun all() = transaction {
        return@transaction ProductEntity.all().map {
            Product(it.id.toString(), it.name, it.price, it.image, it.productStock)
        }.toList()
    }

    fun findByProductId(productId: String) = transaction {
        val productEntity = ProductEntity.find{ProductTable.id eq UUID.fromString(productId)}.singleOrNull()

        return@transaction if (productEntity != null) {
            Product(productEntity.id.toString(), productEntity.name, productEntity.price, productEntity.image, productEntity.productStock)
        } else null
    }
    fun products(limit: Int, start:Long) = transaction {
        ProductEntity.all().limit(limit, start).map {
            Product(it.id.toString(), it.name, it.price, it.image, it.productStock)
        }.toList()

    }


}
