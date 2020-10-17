package com.example.routing

import com.example.constants.AppConstants
import com.example.controllers.UserController
import com.example.jwt.JwtConfig
import com.example.models.Token
import com.example.models.User
import com.example.models.request.UserRequest
import helpers.JsonResponse
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route

private const val API_URL = "api/user/"

fun Route.user(userController: UserController){
     route(API_URL){
        post("registration") {
            val newUser = call.receive<User>()
            when {
                newUser.mobile == null -> {
                    call.respond(JsonResponse.success("mobile is missing", HttpStatusCode.BadRequest))
                }
                newUser.name == null -> {
                    call.respond(JsonResponse.success("name is missing", HttpStatusCode.BadRequest))
                }
                newUser.password == null -> {
                    call.respond(JsonResponse.success("password is missing", HttpStatusCode.BadRequest))
                }
            }
            try {
                val userDb = userController.add(newUser)
                userDb?.let {
                    call.respond(JsonResponse.success(it, HttpStatusCode.OK))
                }
                call.respond(JsonResponse.success("User already exits", HttpStatusCode.OK))
            }catch (e: Throwable){
                call.respond(JsonResponse.failure("Internal server error",  HttpStatusCode.InternalServerError))
            }
        }

         post("login") {
             val user = call.receive<UserRequest>()
             try {
               val userDb = userController.findByMobile(user.mobile)
                 userDb?.let {
                     if (it.password == user.password){
                         val token = JwtConfig.makeToken(it)
                         call.respond(JsonResponse.success(Token(token), HttpStatusCode.OK))
                     }else{
                         call.respond(JsonResponse.failure("Mobile number or password is wrong",  HttpStatusCode.Unauthorized))
                     }
                 }
             }catch (e: Throwable){
                 call.respond(JsonResponse.failure("Internal server error",  HttpStatusCode.BadRequest))
             }

         }
    }
}
