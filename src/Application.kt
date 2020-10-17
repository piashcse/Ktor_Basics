package com.example

import com.example.constants.AppConstants
import com.example.controllers.ProductController
import com.example.controllers.UserController
import com.example.databasehelper.DatabaseFactory
import com.example.jwt.JwtConfig
import com.example.models.User
import com.example.routing.product
import com.example.routing.user
import com.papsign.ktor.openapigen.OpenAPIGen
import com.papsign.ktor.openapigen.openAPIGen
import com.papsign.ktor.openapigen.route.StatusCode
import com.papsign.ktor.openapigen.schema.namer.DefaultSchemaNamer
import com.papsign.ktor.openapigen.schema.namer.SchemaNamer
import io.ktor.application.*
import io.ktor.routing.Routing
import io.ktor.gson.gson
import controllers.FruitController
import helpers.JsonResponse
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.features.*
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Locations
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import org.apache.http.auth.InvalidCredentialsException
import routing.api
import routing.fruits
import kotlin.reflect.KType

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    DatabaseFactory.init()
    val userController = UserController()

    install(StatusPages) {
        exception<MissingRequestParameterException> { exception ->
            call.respond(HttpStatusCode.BadRequest, mapOf("description" to "Parameter missing", "error" to (exception.message ?: "")))
        }
        status(HttpStatusCode.Unauthorized) { statusCode ->
            call.respond(JsonResponse.failure(AppConstants.Error.UNAUTHRIZED,  statusCode))
        }
        status(HttpStatusCode.BadRequest) { statusCode ->
            call.respond(JsonResponse.failure(AppConstants.Error.BAD_REQUEST,  statusCode))
        }
        status(HttpStatusCode.InternalServerError) { statusCode ->
            call.respond(JsonResponse.failure(AppConstants.Error.INTERNAL_SERVER_ERROR,  statusCode))
        }
    }
    install(Compression)
    install(CORS) {
        anyHost()
    }
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(Locations)
    install(Authentication) {
        /**
         * Setup the JWT authentication to be used in [Routing].
         * If the token is valid, the corresponding [User] is fetched from the database.
         * The [User] can then be accessed in each [ApplicationCall].
         */
        jwt {
            verifier(JwtConfig.verifier)
            realm = "ktor.io"
            validate {
               val mobile =  it.payload.getClaim("mobile").asString()
                if (mobile != null){
                     userController.findByMobile(mobile)
                }else{
                    null
                }
            }
        }
    }

    install(OpenAPIGen) {
        // basic info
        info {
            version = "0.0.1"
            title = "Fruit API"
            description = "The Test API"
            contact {
                name = "Mehedi Hassan Piash"
                email = "piash599@gmail.com"
            }
        }
        // describe the server, add as many as you want
        server("http://localhost:8080/") {
            description = "Test server"
        }
        //optional custom schema object name
        replaceModule(DefaultSchemaNamer, object: SchemaNamer {
            val regex = Regex("[A-Za-z0-9_.]+")
            override fun get(type: KType): String {
                return type.toString().replace(regex) { it.value.split(".").last() }.replace(Regex(">|<|, "), "_")
            }
        })
    }
    install(Routing) {
        get("/openapi.json") {
            call.respond(application.openAPIGen.api.serialize())
        }
        get("/2") {
            call.respondRedirect("/swagger-ui/index.html?url=/openapi.json", true)
        }


        api()
        fruits(FruitController())
        user(UserController())
        product(ProductController())
    }
}



