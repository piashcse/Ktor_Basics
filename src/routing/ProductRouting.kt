package com.example.routing

import com.example.constants.AppConstants
import com.example.controllers.ProductController
import com.example.models.Product
import com.example.models.request.ProductRequest
import helpers.JsonResponse
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.product(productController: ProductController){

    route(AppConstants.ProductUrl.BASE_URL) {

        post(AppConstants.ProductUrl.ADD_PRODUCT) {
            val newProduct = call.receive<Product>()
            when {
                newProduct.name == null -> {
                    call.respond(JsonResponse.success("name is missing", HttpStatusCode.BadRequest))
                }
                newProduct.price == null -> {
                    call.respond(JsonResponse.success("name is missing", HttpStatusCode.BadRequest))
                }
                newProduct.image == null -> {
                    call.respond(JsonResponse.success("image is missing", HttpStatusCode.BadRequest))
                }
                newProduct.productStock == null -> {
                    call.respond(JsonResponse.success("productStock is missing", HttpStatusCode.BadRequest))
                }
            }
            try {
                val product = productController.add(newProduct)
                call.respond(JsonResponse.success(product, HttpStatusCode.OK))
            } catch (e: Throwable) {
                call.respond(JsonResponse.failure("Internal server error", HttpStatusCode.InternalServerError))
            }
        }

        get(AppConstants.ProductUrl.PRODUCT_BY_ID) {
            try {
                val id = call.parameters["productId"]
                id?.let {
                    val products = productController.findByProductId(id)
                    call.respond(JsonResponse.success(products, HttpStatusCode.OK))
                }
            } catch (e: Throwable) {
                call.respond(JsonResponse.failure("Internal server error", HttpStatusCode.InternalServerError))
            }
        }

        get(AppConstants.ProductUrl.ALL_PRODUCT) {
            try {
                val products = productController.all()
                call.respond(JsonResponse.success(products, HttpStatusCode.OK))
            } catch (e: Throwable) {
                call.respond(JsonResponse.failure("Internal server error", HttpStatusCode.InternalServerError))
            }
        }

        post(AppConstants.ProductUrl.PRODUCT_PAGINATION) {
            val productParam = call.receive<ProductRequest>()
            when {
                productParam.limit == null -> {
                    call.respond(JsonResponse.success("limit is missing", HttpStatusCode.BadRequest))
                }
                productParam.start == null -> {
                    call.respond(JsonResponse.success("start is missing", HttpStatusCode.BadRequest))
                }
            }
            try {
                val productDb = productController.products(productParam.limit, productParam.start)
                call.respond(JsonResponse.success(productDb, HttpStatusCode.OK))
            } catch (e: Throwable) {
                call.respond(JsonResponse.failure("Internal server error", HttpStatusCode.InternalServerError))
            }
        }

    }
}
