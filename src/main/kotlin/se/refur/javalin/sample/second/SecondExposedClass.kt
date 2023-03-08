package se.refur.javalin.sample.second

import io.javalin.http.HandlerType
import se.refur.javalin.Api
import se.refur.javalin.Page

@Suppress("unused")
class SecondExposedClass {

    @Page(type = HandlerType.GET, path = "/second", templatePath = "", accessRole = "PUBLIC")
    fun pageHandler(): Map<String, Any> = emptyMap()

    @Api(type = HandlerType.GET, path = "/api/second/string", accessRole = "PUBLIC")
    fun apiStringHandler(): String = "API response for SecondExposedClass"

    @Api(type = HandlerType.GET, path = "/api/second/int", accessRole = "PUBLIC")
    fun apiIntHandler(): Int = 42

    @Api(type = HandlerType.GET, path = "/api/second/bool", accessRole = "PUBLIC")
    fun apiBoolHandler(): Boolean = false
}