package routing

import com.example.jwt.JwtConfig
import com.example.models.Token
import com.example.models.User
import com.example.utils.user
import com.papsign.ktor.openapigen.route.StatusCode
import helpers.JsonResponse
import io.ktor.application.call
import io.ktor.auth.UserPasswordCredential
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.auth.*

private const val API_URL = ""

fun Route.api() {
    get(API_URL){
        call.respond("Welcome to Ktor Web API!")
    }
    post("registration") {
        val user = call.receive<User>()
    }
    post("login") {
        val user = call.receive<User>()
        val token = JwtConfig.makeToken(user)
        call.respond(JsonResponse.success(Token(token), HttpStatusCode.OK) )
    }
    authenticate{
        get("/authenticate"){
           try {
               call.respond("get authenticated value from token " +
                       "mobile = ${call.user?.mobile}, name = ${call.user?.name}")
           }catch (e: Throwable){
               call.respond("Not authenticated value from token ")
           }
        }
    }
}
