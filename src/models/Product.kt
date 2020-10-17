package com.example.models

import io.ktor.auth.Principal

data class Product(
        val productID: String,
        val name  : String,
        val price  : Double,
        val image  : String,
        val productStock  : Boolean = false,
)