package se.refur.javalin.sample.second

import io.javalin.http.HandlerType
import se.refur.javalin.Api
import se.refur.javalin.Page
import java.time.LocalDate

@Suppress("unused")
class SecondExposedClass {

    @Page(type = HandlerType.GET, path = "/second", templatePath = "", accessRole = "PUBLIC")
    fun pageHandler(): Map<String, Any> = emptyMap()

    @Api(type = HandlerType.GET, path = "/api/second/string", accessRole = "PUBLIC")
    fun apiStringHandler(): String = "Validate: åäöÅÄÖ%&?"

    @Api(type = HandlerType.GET, path = "/api/second/int", accessRole = "PUBLIC")
    fun apiIntHandler(): Int = 42

    @Api(type = HandlerType.GET, path = "/api/second/bool", accessRole = "PUBLIC")
    fun apiBoolHandler(): Boolean = false

    @Api(type = HandlerType.GET, path = "/api/second/obj", accessRole = "PUBLIC")
    fun apiObjectHandler(): MyDataClass = MyDataClass(
        name = "Someone",
        birthDay = LocalDate.parse("1971-01-01"),
        heightCm = 177,
        isGood = true)
}

data class MyDataClass(
    val name: String,
    val birthDay: LocalDate,
    val heightCm: Int,
    val isGood: Boolean
)