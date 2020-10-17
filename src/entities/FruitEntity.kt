package entities

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object Fruits : IntIdTable("fruit") {
    val no = Fruits.text("no").uniqueIndex()
    val description = Fruits.text("description")
}

class FruitEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FruitEntity>(Fruits)

    var no by Fruits.no
    var description by Fruits.description
}
