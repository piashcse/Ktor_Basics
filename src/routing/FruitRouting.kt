package routing

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import controllers.FruitController
import de.nielsfalk.ktor.swagger.version.shared.Group
import helpers.JsonResponse
import io.ktor.http.HttpStatusCode
import models.Fruit

private const val API_URL = "api/fruits"

fun Route.fruits(controller: FruitController) {
    route(API_URL) {
        get {
            val fruits = controller.all()

            val response = JsonResponse.success(fruits, HttpStatusCode.OK)

            call.respond(response)
        }
        get("{id}") {
            val id = call.parameters["id"]!!.toInt()

            val fruit = controller.one(id)

            val response = if (fruit == null) {
                JsonResponse.failure("Fruit does not exists", HttpStatusCode.NotFound)
            } else {
                JsonResponse.success(fruit,  HttpStatusCode.OK)
            }

            call.respond(response)
        }
        post {
            val newFruit = call.receive<Fruit>()

            val fruit = controller.new(newFruit)

            val response = if (fruit == null) {
                JsonResponse.failure("Fruit already exists",  HttpStatusCode.NotFound)
            } else {
                JsonResponse.success(fruit, HttpStatusCode.OK)
            }

            call.respond(response)
        }

        put("{id}") {
            val id = call.parameters["id"]!!.toInt()

            val editFruit = call.receive<Fruit>()

            val fruit = controller.edit(id, editFruit)

            val response = if (fruit == null) {
                JsonResponse.failure("Fruit does not exists",  HttpStatusCode.NotFound)
            } else {
                JsonResponse.success(fruit,  HttpStatusCode.OK)
            }

            call.respond(response)
        }

        delete("{id}") {
            val id = call.parameters["id"]!!.toInt()

            val fruit = controller.delete(id)

            val response = if (fruit == null) {
                JsonResponse.failure("Fruit does not exists",  HttpStatusCode.NotFound)
            } else {
                JsonResponse.success(fruit,  HttpStatusCode.OK)
            }

            call.respond(response)
        }
    }
}
