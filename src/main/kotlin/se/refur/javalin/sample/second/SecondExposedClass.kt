package se.refur.javalin.sample.second

import io.javalin.http.HandlerType
import se.refur.javalin.Api
import se.refur.javalin.Page

class SecondExposedClass {

    @Page(type = HandlerType.GET, path = "/second", templatePath = "", accessRole = "PUBLIC")
    fun pageHandler(): Map<String, Any> = emptyMap()

    @Api(type = HandlerType.GET, path = "/api/second", accessRole = "PUBLIC")
    fun apiHandler(): String = "API response for SecondExposedClass"

}