package com.example.models

import io.ktor.auth.Principal

data class User(
    val id: Int,
    val name: String,
    val countries: List<String>
) : Principal