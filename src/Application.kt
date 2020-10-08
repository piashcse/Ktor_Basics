package com.example

import com.example.databasehelper.DatabaseFactory
import com.papsign.ktor.openapigen.OpenAPIGen
import com.papsign.ktor.openapigen.content.type.binary.BinaryContentTypeParser.respond
import com.papsign.ktor.openapigen.openAPIGen
import com.papsign.ktor.openapigen.schema.namer.DefaultSchemaNamer
import com.papsign.ktor.openapigen.schema.namer.SchemaNamer
import io.ktor.application.*
import io.ktor.routing.Routing
import io.ktor.gson.gson
import controllers.FruitController
import io.ktor.features.*
import io.ktor.locations.Locations
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import routing.api
import routing.fruits
import kotlin.reflect.KType
val sizeSchemaMap = mapOf(
        "type" to "number",
        "minimum" to 0
)
fun rectangleSchemaMap(refBase: String) = mapOf(
        "type" to "object",
        "properties" to mapOf(
                "a" to mapOf("${'$'}ref" to "$refBase/size"),
                "b" to mapOf("${'$'}ref" to "$refBase/size")
        )
)


const val petUuid = "petUuid"

val petIdSchema = mapOf(
        "type" to "string",
        "format" to "date",
        "description" to "The identifier of the pet to be accessed"
)
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
   /* val appConfig = environment.config

    val databaseUrl = appConfig.property("database.url").getString()
    val databaseDriver = appConfig.property("database.driver").getString()

    Database.connect(databaseUrl, databaseDriver)
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE*/

    DatabaseFactory.init()
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
    install(OpenAPIGen) {
        // basic info
        info {
            version = "0.0.1"
            title = "Fruit API"
            description = "The Test API"
            contact {
                name = "Mehedi Hassan Piash"
                email = "support@test.com"
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
    /*install(SwaggerSupport) {
        forwardRoot = true
        val information = Information(
                version = "0.1",
                title = "sample api implemented in ktor",
                description = "This is a sample which combines [ktor](https://github.com/Kotlin/ktor) with [swaggerUi](https://swagger.io/). You find the sources on [github](https://github.com/nielsfalk/ktor-swagger)",
                contact = Contact(
                        name = "Niels Falk",
                        url = "https://nielsfalk.de"
                )
        )
        swagger = Swagger().apply {
            info = information
            definitions["size"] = sizeSchemaMap
            definitions[petUuid] = petIdSchema
            definitions["Rectangle"] = rectangleSchemaMap("#/definitions")
        }
        openApi = OpenApi().apply {
            info = information
            components.schemas["size"] = sizeSchemaMap
            components.schemas[petUuid] = petIdSchema
            components.schemas["Rectangle"] = rectangleSchemaMap("#/components/schemas")
        }
    }*/
    install(Routing) {
        api()
        fruits(FruitController())
        get("/openapi.json") {
            call.respond(application.openAPIGen.api.serialize())
        }
        get("/2") {
            call.respondRedirect("/swagger-ui/index.html?url=/openapi.json", true)
        }
    }
}



