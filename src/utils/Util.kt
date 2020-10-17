package com.example.utils

import com.example.models.User
import io.ktor.application.ApplicationCall
import io.ktor.auth.authentication
val ApplicationCall.user get() = authentication.principal<User>()
