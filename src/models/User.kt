package com.example.models

import io.ktor.auth.Principal

data class User(
        val mobile: String,
        val name: String,
        val password: String
) : Principal