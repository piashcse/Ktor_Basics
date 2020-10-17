package com.example.models.request

import io.ktor.auth.Principal

data class UserRequest(
        val mobile: String,
        val password: String
) : Principal