package com.example.databasehelper
import com.example.entities.ProductTable
import com.example.entities.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import entities.Fruits
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create

object DatabaseFactory {
    fun init(){
       // Database.connect(hikari())
        initDB()
        transaction {
            create(Fruits, UserTable, ProductTable)
        }
    }

  private  fun initDB() {
        // database connection is handled from hikari properties
        val config = HikariConfig("/hikari.properties")
        val ds = HikariDataSource(config)
        Database.connect(ds)
    }

    // database connection for h2 d
    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.h2.Driver"
        config.jdbcUrl = "jdbc:h2:file:~/documents/db/h2db"
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }
}