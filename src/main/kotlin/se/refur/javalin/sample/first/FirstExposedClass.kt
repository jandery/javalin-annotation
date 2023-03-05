package se.refur.javalin.sample.first

import io.javalin.http.HandlerType
import se.refur.javalin.Api
import se.refur.javalin.Page

@Suppress("unused")
class FirstExposedClass {

    @Page(type = HandlerType.GET, path = "/first", templatePath = "", accessRole = "PUBLIC")
    fun pageHandler(): Map<String, Any> = emptyMap()

    @Api(type = HandlerType.GET, path = "/api/first", accessRole = "PUBLIC")
    fun apiHandler(): String = "API response for FirstExposedClass"
}